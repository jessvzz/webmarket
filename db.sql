/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


DROP DATABASE IF EXISTS `webdb2`;
CREATE DATABASE `webdb2`; 
DROP USER IF EXISTS 'website'@'localhost';
CREATE USER 'website'@'localhost' IDENTIFIED BY 'webpass';
GRANT ALL PRIVILEGES ON webdb2.* TO 'website'@'localhost';

USE `webdb2`;

/* CREAZIONE TABELLE */

CREATE TABLE categoria (
    ID int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    nome varchar(255) UNIQUE NOT NULL,
    version BIGINT UNSIGNED NOT NULL DEFAULT 1,
    padre int(11) DEFAULT NULL,
    CONSTRAINT categoria_padre FOREIGN KEY (padre)
        REFERENCES categoria(ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE caratteristica (
    ID int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    nome varchar(255) NOT NULL,
    version BIGINT UNSIGNED NOT NULL DEFAULT 1,
    categoria_id int(11) NOT NULL,
    CONSTRAINT categoria_caratteristica FOREIGN KEY (categoria_id)
        REFERENCES categoria(ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE utente (
    ID int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    username varchar(255) UNIQUE NOT NULL,
    email varchar(255) NOT NULL,
    password varchar(255) NOT NULL,
    tipologia_utente ENUM('ORDINANTE', 'TECNICO', 'AMMINISTRATORE') NOT NULL,
    version BIGINT UNSIGNED NOT NULL DEFAULT 1
);

CREATE TABLE richiesta_ordine (
    ID int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    note varchar(255),
    stato ENUM('IN_ATTESA','PRESA_IN_CARICO','RISOLTA','ORDINATA') NOT NULL,
    data DATE,
    codice_richiesta varchar(255) UNIQUE NOT NULL,
    version BIGINT UNSIGNED NOT NULL DEFAULT 1,
    utente int(11) NOT NULL,
    tecnico int(11),
    categoria_id int(11) NOT NULL,
    CONSTRAINT id_utente FOREIGN KEY (utente)
        REFERENCES utente(ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT id_tecnico FOREIGN KEY (tecnico)
        REFERENCES utente(ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT id_categoria FOREIGN KEY (categoria_id)
        REFERENCES categoria(ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE caratteristica_richiesta ( /* "compone" */
    ID int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    richiesta_id int(11) NOT NULL,
    caratteristica_id int(11) NOT NULL,
    valore varchar(200) NOT NULL DEFAULT 'Indifferente',
    version BIGINT UNSIGNED NOT NULL DEFAULT 1,
    CONSTRAINT id_richiesta FOREIGN KEY (richiesta_id)
        REFERENCES richiesta_ordine(ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE,
    CONSTRAINT id_caratteristica FOREIGN KEY (caratteristica_id)
        REFERENCES caratteristica(ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE proposta_acquisto (
    ID int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    produttore varchar(500) NOT NULL,
    prodotto varchar(500) NOT NULL,
    codice varchar(500) NOT NULL,
    codice_prodotto varchar(50) NOT NULL,
    prezzo float NOT NULL,
    URL text NOT NULL,
    note varchar(255),
    stato ENUM('ACCETTATO','RIFIUTATO','IN_ATTESA','ORDINATO') NOT NULL,
    data DATE,
    motivazione text DEFAULT NULL,
    version BIGINT UNSIGNED NOT NULL DEFAULT 1,
    richiesta_id int(11) NOT NULL,
    CONSTRAINT id_richiesta_proposta FOREIGN KEY (richiesta_id)
        REFERENCES richiesta_ordine(ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE ordine (
    ID int(11) NOT NULL PRIMARY KEY AUTO_INCREMENT,
    data DATE,
    stato ENUM('IN_ATTESA','ACCETTATO','RESPINTO_NON_CONFORME','RESPINTO_NON_FUNZIONANTE', 'RIFIUTATO') NOT NULL,
    version BIGINT UNSIGNED NOT NULL DEFAULT 1,
    proposta_id int(11) NOT NULL,
    CONSTRAINT id_proposta FOREIGN KEY (proposta_id)
        REFERENCES proposta_acquisto(ID)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);


/* FUNCTIONS */

/* per generare un codice alfanumerico automaticamente */
DROP FUNCTION IF EXISTS generate_codice;
DELIMITER $$

CREATE FUNCTION generate_codice()
RETURNS CHAR(10)
DETERMINISTIC
BEGIN
    DECLARE chars CHAR(62) DEFAULT 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    DECLARE result CHAR(10) DEFAULT '';
    DECLARE i INT DEFAULT 0;

    WHILE i < 10 DO
        SET result = CONCAT(result, SUBSTRING(chars, FLOOR(1 + RAND() * 62), 1));
        SET i = i + 1;
    END WHILE;

    RETURN result;
END$$

DELIMITER ;


/* per evitare cicli nella struttura ad albero delle categorie */
DROP FUNCTION IF EXISTS isCyclic;
DELIMITER $$

CREATE FUNCTION isCyclic(new_parent_id INT, current_id INT)
RETURNS BOOLEAN
DETERMINISTIC
BEGIN
    DECLARE parent INT;
    SET parent = new_parent_id;

    WHILE parent IS NOT NULL DO
        IF parent = current_id THEN
            RETURN TRUE;
        END IF;
        SELECT padre INTO parent FROM categoria WHERE ID = parent LIMIT 1;
    END WHILE;

    RETURN FALSE;
END$$

DELIMITER ;


/* TRIGGERS */

/* per generare codice automaticamente in proposta_acquisto */
DROP TRIGGER IF EXISTS before_insert_proposta;

DELIMITER $$

CREATE TRIGGER before_insert_proposta
BEFORE INSERT ON proposta_acquisto
FOR EACH ROW
BEGIN
    IF NEW.codice IS NULL OR NEW.codice = '' THEN
        SET NEW.codice = generate_codice();
    END IF;
END$$

DELIMITER ;


/* per generare codice automaticamente in richiesta_ordine*/
DROP TRIGGER IF EXISTS before_insert_richiesta;

DELIMITER $$

CREATE TRIGGER before_insert_richiesta
BEFORE INSERT ON richiesta_ordine
FOR EACH ROW
BEGIN
    IF NEW.codice_richiesta IS NULL OR NEW.codice_richiesta = '' THEN
        SET NEW.codice_richiesta = generate_codice();
    END IF;
END$$

DELIMITER ;


/* per evitare cicli nella struttura ad albero delle categorie */
/* insert */
DROP TRIGGER IF EXISTS before_categoria_insert;
DELIMITER $$

CREATE TRIGGER before_categoria_insert
BEFORE INSERT ON categoria
FOR EACH ROW
BEGIN
    IF NEW.padre IS NOT NULL AND isCyclic(NEW.padre, NEW.ID) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Cycle detected: Invalid parent_id';
    END IF;
END$$

DELIMITER ;

/* update */
DROP TRIGGER IF EXISTS before_categoria_update;
DELIMITER $$

CREATE TRIGGER before_categoria_update
BEFORE UPDATE ON categoria
FOR EACH ROW
BEGIN
    IF NEW.padre IS NOT NULL AND isCyclic(NEW.padre, NEW.ID) THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Cycle detected: Invalid parent_id';
    END IF;
END$$

DELIMITER ;


/* POPOLAMENTO TABELLE */

INSERT INTO utente (username, email, password, tipologia_utente) VALUES
    ('admin', 'admin@example.com', '282db4a4425f50237e7df29d56988825f15dd8b34fa74af54e650ce0fd8897a82dff0b952017a3a88a62f5f1b0e0e467', 'AMMINISTRATORE'), #admin
    ('tecnicog', 'difgiulia@gmail.com', 'aec1e7153435fb922c594376ca7e0ed24d0e8ad306648ba4a2153165941157d3a2a80649d70780821a4b8b6e5e8bb4a6', 'TECNICO'), #tecnico1
    ('geaviozzi', 'geaviozzi@icloud.com', '3ece874c150039c7740d2d5c74ecf2f0b64c3a6c972bbd1b389ca3db8402bdb5a783084ac59a1015c793f4c24a8fbcb2', 'ORDINANTE'), #passgea
    ('samanta', 'samanta_dis@hotmail.com', '63a7d1207dab49f5cdc13091e8a87006097ca0c7fecba40b316d8571d415a7d5109e48bdba061a98fa7ff551717fd79a', 'ORDINANTE'), #samanta
    ('tecnicoS', 'samanta_dis@hotmail.com', '277fb18292a23f495c1f5ffd79af834aa2107d47ea2f7db6b7d1ac3098d451a6bbee64f6e041d7b0850791ec3a5cded9', 'TECNICO'), #tecnicoS
    ('userOrd', 'giuseppe.dellapenna@univaq.it', 'd494f861ac83dc88d6dac053dccd14bdb49e6e985ee0f4f86d85f07d3c01064d24ce9d7806a369fbd18a93a7702cecb3', 'ORDINANTE'), #example
    ('userTec', 'giuseppe.dellapenna@univaq.it', 'd494f861ac83dc88d6dac053dccd14bdb49e6e985ee0f4f86d85f07d3c01064d24ce9d7806a369fbd18a93a7702cecb3', 'TECNICO'); #example

INSERT INTO categoria (nome, padre) VALUES
    ('Informatica', NULL),
    ('Computer', 1),
    ('Notebook', 2),
    ('Desktop', 2),
    ('Periferiche', 1),
    ('Monitor', 5),
    ('Stampanti', 5),
    ('Mobili', NULL),
    ('Scrivanie', 8),
    ('Sedie', 8),
    ('Cancelleria', NULL),
    ('Quaderni', 11),
    ('Agende', 11);

INSERT INTO caratteristica (nome, categoria_id) VALUES
	('Sistema Op supportato', 1), -- informatica
	('Scheda Grafica', 2), -- Computer
    ('RAM', 3), -- Notebook
    ('CPU', 3), -- Notebook
    ('Formato del case', 4), -- Desktop
    ('Tipo di dispositivo', 5), -- Periferiche
    ('Tipo di risoluzione', 5), -- periferiche
    ('Dimensione Schermo', 6), -- Monitor
    ('Risoluzione', 6), -- Monitor
    ('Tipo di Inchiostro', 7), -- Stampanti
    ('Velocità di Stampa', 7), -- Stampanti
    ('Tipo', 8), -- mobili
    ('Materiale', 9), -- Scrivanie
    ('Colore', 9), -- Scrivanie
    ('Peso Massimo Supportato', 10), -- Sedie
    ('Tipo', 11), -- cancelleria
    ('Formato', 12),
    ('Formato', 13);

INSERT INTO richiesta_ordine (note, stato, data, codice_richiesta, utente, tecnico, categoria_id) VALUES
    ('Acquisto nuovo notebook', 'RISOLTA', '2024-09-01', generate_codice(), 3, 2, 3),
    ('Acquisto scrivania', 'PRESA_IN_CARICO', '2024-08-25', generate_codice(), 3, 2, 9),
    ('Monitor esterno per ufficio', 'RISOLTA', '2024-08-15', generate_codice(), 4, 5, 6),
    ('Stampante multifunzione', 'ORDINATA', '2024-07-30', generate_codice(), 4, 2, 7),
    ('Acquisto sedia ergonomica', 'IN_ATTESA', '2024-09-02', generate_codice(), 3, NULL, 10), 
    ('Lo vorrei leggero', 'ORDINATA', '2024-09-02', generate_codice(), 6, 7, 3),
    ('Ergonomica contro il mal di schiena', 'IN_ATTESA', '2024-09-03', generate_codice(), 6, NULL, 10),
    (NULL, 'PRESA_IN_CARICO', '2024-09-04', generate_codice(), 6, 7, 12),
    ('Schemo curvo', 'RISOLTA', '2024-09-05', generate_codice(), 6, 7, 6),
    ('magic mouse', 'PRESA_IN_CARICO', '2024-09-06', generate_codice(), 6, 7, 5),
    ('Deve essere abbastanza grande per il mio fisso, il mio pranzo e la mia borsa da lavoro', 'IN_ATTESA', '2024-09-07', generate_codice(), 6, 7, 7);

INSERT INTO caratteristica_richiesta (richiesta_id, caratteristica_id, valore) VALUES
    (1, 1, '16GB'), -- Notebook (RAM)
    (1, 2, 'Intel i7'), -- Notebook (CPU)
    (2, 7, 'Legno'), -- Scrivania (Materiale)
    (2, 8, 'Bianco'), -- Scrivania (Colore)
    (3, 3, '27 pollici'), -- Monitor (Dimensione Schermo)
    (3, 4, '4K'), -- Monitor (Risoluzione)
    (4, 5, 'Laser'), -- Stampanti (Tipo di Inchiostro)
    (4, 6, '40 pagine al minuto'), -- Stampanti (Velocità di Stampa)
    (5, 9, '120 kg'), -- Sedia (Peso Massimo Supportato)
    (6, 3, '16GB'),
    (6, 4, 'intel'),
    (7, 15, '100kg'),
    (8, 17, 'quadretti'),
    (9, 8, 'indifferente'),
    (9, 9, '4K'),
    (10, 6, 'mouse'),
    (10, 7, 'indifferente'),
    (11, 13, 'Legno'),
    (11, 14, 'Indifferente');
    

INSERT INTO proposta_acquisto (produttore, prodotto, codice, codice_prodotto, prezzo, URL, note, stato, data, motivazione, richiesta_id) VALUES
    ('Dell', 'Notebook XPS 15', generate_codice(), 'XPS-2024', 1500.50, 'https://dell.com/notebook-xps15', 'Perfetto per ufficio', 'ORDINATO', '2024-09-03', NULL,  1),
    ('Ikea', 'Scrivania LINNMON', generate_codice(), 'LINNMON-2024', 89.99, 'https://ikea.com/scrivania-linnmon', 'Colore bianco, misura 120x60 cm', 'IN_ATTESA', '2024-09-01', NULL, 2),
    ('Samsung', 'Monitor 27" UHD', generate_codice(), 'SAM-UHD-27', 299.99, 'https://samsung.com/monitor-uhd27', 'Schermo UHD 4K', 'ORDINATO', '2024-08-18', NULL, 3),
    ('HP', 'Stampante LaserJet Pro', generate_codice(), 'LASERPRO-2024', 199.99, 'https://hp.com/stampante-laserjet-pro', 'Laser, alta velocità', 'ORDINATO', '2024-08-01', NULL, 4),
    ('HP', 'HP Pavilion 15-eh3008nl', generate_codice(), '83A27EA#ABZ', 629.98, 'https://www.hp.com/it-it/shop/product.aspx?id=83A27EA&opt=ABZ&sel=NTB&gad_source=1&gclid=Cj0KCQjw8--2BhCHARIsAF_w1gzGUkreM3_06TgYvfdYv0Ll5zyRO9GfFK0wqn2f7FK6zzFTIxZQ91AaAlO4EALw_wcB&gclsrc=aw.ds', NULL, 'RIFIUTATO', '2024-09-03', 'Non mi piace il produttore HP', 6),
    ('Pigna Monocromo', 'Quaderno formato A4', generate_codice(), 'n/a', 14.99, 'https://www.amazon.it/Pigna-Monocromo-02298875M-Quadretti-Elementare/dp/B07GR9LQSK/ref=asc_df_B07GR9LQSK/?tag=googshopit-21&linkCode=df0&hvadid=701238956335&hvpos=&hvnetw=g&hvrand=12216248457852547069&hvpone=&hvptwo=&hvqmt=&hvdev=c&hvdvcmdl=&hvlocint=&hvlocphy=9050671&hvtargid=pla-533311440398&mcid=6540bcbf14773a68b184db22a11bf770&gad_source=1&th=1', 'disponibile in diversi colori', 'IN_ATTESA',  '2024-09-05', NULL, 8),
    ('Apple', 'Apple Magic Mouse', generate_codice(), 'tYy3BaDB5i', 75.00, 'https://www.amazon.it/Apple-MK2E3ZM-A-Magic-Mouse/dp/B09BV7YYG3/ref=sr_1_3?__mk_it_IT=%C3%85M%C3%85%C5%BD%C3%95%C3%91&crid=1JR7AGS0VAY3Z&dib=eyJ2IjoiMSJ9.3aecOhkwpcmvt_QDJguKiv9022QFwHUAXnJ7Ma93QSrbXLBtbNGpCmcvqzJ9tAtFYjnvAqLdOQZyl1rbt5_5SnRF7gww_uKDkXiXbZmfYiQR7cgOADBucCX_m_zj0cJ1DE5o69ZcMMvX2B10914oxuGl7lK3AaHzehd30heTX5SRQYFRJHLSXXx7ADtv52T2-BbCVu-1TvEjT03m2XwsZ41csrIWOrL8Q4Lk8IYU3IBT5ni5SzOMm8A4KiEWHQLzrcJuynWnhXfKAp_N_jE2N7cXPTy5r-FleFSqmEWETo8.jAyKWBW_lGd-tXrNPMUOuyOQmT7EJsNtgZfd-9Dlu3A&dib_tag=se&keywords=magic+mouse&qid=1725697145&s=pc&sprefix=magic+mouse%2Ccomputers%2C90&sr=1-3', NULL, 'ORDINATO', '2024-09-06', NULL, 9),
    ('LENOVO', 'Notebook 15" IdeaPad', generate_codice(), 'N/A', 499.98, 'https://www.euronics.it/informatica/computer-portatili/notebook/lenovo---notebook-15-ideapad-slim-3-amd-ryzen5-16gb-512gb-artic-grey/232002072.html?gad_source=1&gclid=Cj0KCQjw8--2BhCHARIsAF_w1gx_23whh278MLJM2UCalVHNyGg61giUydkv-WgpyeO66ELnwfB3siYaAsHxEALw_wcB', 'Ottimo prezzo', 'ORDINATO', '2024-09-04', NULL, 6),
    ('Castellani Shop', 'Scrivania Ufficio', generate_codice(), 'fhbjdn', 122.24, 'https://www.castellanishop.it/scrivania-eco-con-fianchi-pannellati-in-melaminico-cm-80-140-160x80x72h.html?fee=1&fep=6239&gad_source=1&gclid=Cj0KCQjw8--2BhCHARIsAF_w1gwsuG_Cy4iofUmi-0B_VfxmG2ImfWX59-z6XO8jKLbLpinfXCJUUCQaAhG4EALw_wcB', NULL, 'ACCETTATO', '2024-09-07', NULL, 11);

INSERT INTO ordine (data, stato, proposta_id) VALUES
    ('2024-09-05', 'ACCETTATO', 1), -- Notebook Dell
    ('2024-08-20', 'ACCETTATO', 3), -- Monitor Samsung
    ('2024-08-05', 'IN_ATTESA', 4), -- Stampante HP
    ('2024-09-06', 'ACCETTATO', 7),
    ('2024-09-07', 'IN_ATTESA', 8),
    ('2024-09-07', 'RESPINTO_NON_CONFORME', 9);