package it.univaq.f4i.iw.ex.webmarket.data.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.univaq.f4i.iw.ex.webmarket.data.dao.CategoriaDAO;
import it.univaq.f4i.iw.ex.webmarket.data.model.Categoria;
import it.univaq.f4i.iw.ex.webmarket.data.model.impl.proxy.CategoriaProxy;
import it.univaq.f4i.iw.framework.data.DAO;
import it.univaq.f4i.iw.framework.data.DataException;
import it.univaq.f4i.iw.framework.data.DataItemProxy;
import it.univaq.f4i.iw.framework.data.DataLayer;

/**
 *
 * @author Giulia Di Flamminio
 */

public class CategoriaDAO_MySQL extends DAO implements CategoriaDAO {

    private PreparedStatement sCategoriaByID;
    private PreparedStatement sCategoriaByNome;
    private PreparedStatement sCategoriaByCaratteristica;

    private PreparedStatement iCategoria;

    private PreparedStatement uCategoria;

    private PreparedStatement sCategorie;
    private PreparedStatement sCategorieFiglioFromPadre;

    private PreparedStatement dCategoria;
    


    public CategoriaDAO_MySQL(DataLayer d) {
        super(d);
    }

    @Override
    public void init() throws DataException {
        try {
            super.init();
     
            sCategoriaByID = connection.prepareStatement("SELECT * FROM categoria WHERE ID=?");
            sCategoriaByNome = connection.prepareStatement("SELECT ID FROM categoria WHERE nome=?");
            sCategoriaByCaratteristica = connection.prepareStatement("SELECT categoria.ID FROM categoria, caratteristica WHERE categoria.ID=caratteristica.categoria_id AND caratteristica.ID=?");

            iCategoria = connection.prepareStatement("INSERT INTO categoria(nome, padre) VALUES (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);

            uCategoria = connection.prepareStatement("UPDATE categoria SET nome=? WHERE ID=?");
            
            sCategorie = connection.prepareStatement("SELECT nome FROM categoria");
            
            sCategorieFiglioFromPadre = connection.prepareStatement("SELECT * FROM categoria WHERE padre = ?");

            dCategoria = connection.prepareStatement("DELETE FROM categoria WHERE ID=?");

        } catch (SQLException e) {
            throw new DataException("Error initializing webmarket data layer", e);
        }
    }

    @Override
    public void destroy() throws DataException {
        try {
            sCategoriaByID.close();
            sCategoriaByNome.close();
            sCategoriaByCaratteristica.close();

            iCategoria.close();

            uCategoria.close();

            sCategorieFiglioFromPadre.close();

            dCategoria.close();

        } catch (SQLException ex) {
            throw new DataException("Can't destroy prepared statements", ex);
        }
        super.destroy();
    }

    @Override
    public Categoria createCategoria() {
        return new CategoriaProxy(getDataLayer());
    }

    private Categoria createCategoria(ResultSet rs) throws DataException {
        try {
            CategoriaProxy cp = (CategoriaProxy) createCategoria();
            cp.setKey(rs.getInt("ID"));
            cp.setNome(rs.getString("nome"));
            cp.setPadre(rs.getInt("padre"));
            return cp;
        } catch (SQLException ex) {
            throw new DataException("Unable to create Categoria object form ResultSet", ex);
        }
    }

    @Override
    public Categoria getCategoria(int categoria_key) throws DataException {
        Categoria cp = null;
        if (dataLayer.getCache().has(Categoria.class, categoria_key)) {
            cp = dataLayer.getCache().get(Categoria.class, categoria_key);
        } else {
            try {
                sCategoriaByID.setInt(1, categoria_key);
                try (ResultSet rs = sCategoriaByID.executeQuery()) {
                    if (rs.next()) {
                        cp = createCategoria(rs);
                        dataLayer.getCache().add(Categoria.class, cp);
                    }
                }
            } catch (SQLException ex) {
                throw new DataException("Unable to load Categoria by ID", ex);
            }
        }
        return cp;
    }

    @Override
    public List<Categoria> getAllCategorie() throws DataException {
    List<Categoria> result = new ArrayList<>();
        try {
            try (ResultSet rs = sCategorie.executeQuery()) {
                while (rs.next()) {
                    result.add(getCategoria(rs.getInt("ID")));
                }
            }
            return result;
        } catch (SQLException ex) {
            throw new DataException("Error loading all categories", ex);
        }
    }

    @Override
    public void storeCategoria(Categoria categoria) throws DataException {
        try {
            if (categoria.getKey() != null && categoria.getKey() > 0) { //update
                /*if (categoria instanceof DataItemProxy && !((DataItemProxy) categoria).isModified()) {
                    return;
                }*/
                System.out.println("Sono qui e la categoria è:" + categoria.getKey() );
                uCategoria.setString(1, categoria.getNome());
                uCategoria.setInt(2, categoria.getKey());
                uCategoria.executeUpdate();
            } else { //insert
                iCategoria.setString(1, categoria.getNome());
                iCategoria.setInt(2, categoria.getPadre());

                if (iCategoria.executeUpdate() == 1) {
                    try (ResultSet keys = iCategoria.getGeneratedKeys()) {
                        if (keys.next()) {
                            int key = keys.getInt(1);
                            categoria.setKey(key);
                            dataLayer.getCache().add(Categoria.class, categoria);
                        }
                    }
                }
            }
            if (categoria instanceof DataItemProxy) {
                ((DataItemProxy) categoria).setModified(false);
            }
        } catch (SQLException ex) {
            throw new DataException("Unable to store Categoria", ex);
        }
    }

    @Override
    public void deleteCategoria(Categoria categoria) throws DataException {
        try {
            dataLayer.getCache().delete(Categoria.class, categoria);
            dCategoria.setInt(1, categoria.getKey());
            dCategoria.executeUpdate();

        } catch (SQLException e) {
            throw new DataException("Unable to delete Categoria", e);
        }
    }
    
    @Override
    public List<Categoria> getCategorieByPadre(int padre) throws DataException {
    List<Categoria> result = new ArrayList<>();
    try {
        sCategorieFiglioFromPadre.setInt(1, padre);
        try (ResultSet rs = sCategorieFiglioFromPadre.executeQuery()) {
            while (rs.next()) {
                result.add(createCategoria(rs));
            }
        }
    } catch (SQLException ex) {
        throw new DataException("Unable to load child categories by parent ID", ex);
    }
    return result;
}

}