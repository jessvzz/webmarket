package it.univaq.f4i.iw.framework.data;

/**
 *
 * @author giuse
 * @param <KT> the key type
 */
public interface DataItem<KT> {

    KT getKey();

    long getVersion();

    void setKey(KT key);

    void setVersion(long version);

}
