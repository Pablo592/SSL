package ar.edu.iua.segInfo.modelo.cache;

import ar.edu.iua.segInfo.modelo.NumeroPrimo;
import net.spy.memcached.MemcachedClient;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;

@Component
public class Memcache{

private   MemcachedClient mcc;


    public Memcache() {

        try {
            this.mcc = new MemcachedClient(new InetSocketAddress("localhost",11311));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Connecting to Memcached server on localhost
}

    public String buscar(String id){
        String dato = "";
        dato = (String) mcc.get(id);
        return mcc.get(id) == null ? null : dato;
    }



    public boolean agregar(long numero, String key, int tiempo){
        return  mcc.add(key, tiempo, String.valueOf(numero)).isDone();
    }


    public boolean actualizar(NumeroPrimo numeroPrimo,int tiempo){
        return  mcc.set("", tiempo, "").isDone();
    }


    public boolean eliminar(Long id){
        return mcc.delete(String.valueOf(id)).isDone();
    }
}