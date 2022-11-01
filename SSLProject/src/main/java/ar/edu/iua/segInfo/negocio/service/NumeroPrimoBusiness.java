package ar.edu.iua.segInfo.negocio.service;

import ar.edu.iua.segInfo.modelo.NumeroPrimo;
import ar.edu.iua.segInfo.modelo.cache.Memcache;
import ar.edu.iua.segInfo.modelo.dto.ClavePublicaDTO;
import ar.edu.iua.segInfo.modelo.dto.Criptograma;
import ar.edu.iua.segInfo.modelo.repository.NumerosPrimosRepository;
import ar.edu.iua.segInfo.negocio.excepciones.NegocioException;
import ar.edu.iua.segInfo.negocio.excepciones.NoEncontradoException;
import ar.edu.iua.segInfo.util.MensajeRespuesta;
import ar.edu.iua.segInfo.util.RespuestaGenerica;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Service
public class NumeroPrimoBusiness implements INumeroPrimoBusiness {

    @Autowired
    private Memcache cache;

    private Logger log = LoggerFactory.getLogger(NumeroPrimoBusiness.class);

    @Autowired
    private NumerosPrimosRepository numerosPrimosDAO;


    @Override
    public NumeroPrimo load(Long id) throws NoEncontradoException, NegocioException {
        Optional<NumeroPrimo> op;
        try {
            op =  numerosPrimosDAO.findById(id);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new NegocioException(e);
        }
        if (!op.isPresent()) {
            throw new NoEncontradoException("El numero primo no se encuentra en la BD");
        }
        return op.get();
    }

    @Override
    public ClavePublicaDTO publicKey() throws NoEncontradoException, NegocioException {

   // long id_R = (long)Math.floor(Math.random()*(5761445-4000000+1)+4000000);

        long id_R = (long)Math.floor(Math.random()*(10-1+1)+1);
    long num_R = load(id_R).getNumero();
    //long num_R_mayor = load(id_R+10).getNumero();
        long num_R_mayor = load(id_R+2).getNumero();
    long num_p = 2*num_R + 1;
    long num_q = 2*num_R_mayor + 1;
    long fi_N = (num_p-1)*(num_q-1);
    long clavePublica_n = num_p*num_q;
    long clavePublica_e = 0;


    do{
       // clavePublica_e = (long)Math.floor(Math.random()*(fi_N-15000000+1)+15000000);
        clavePublica_e = (long)Math.floor(Math.random()*(fi_N-1+1)+1);
    }while (mcd(clavePublica_e,clavePublica_n) != 1);

        ClavePublicaDTO clavePublica = new ClavePublicaDTO(clavePublica_e,clavePublica_n);

        cache.agregar(num_p,"num_p",3600);
        cache.agregar(num_q,"num_q",3600);
        cache.agregar(clavePublica_n,"clavePublica_n",3600);
        cache.agregar(clavePublica_e,"clavePublica_e",3600);
        cache.agregar(fi_N,"fi_N",3600);


        return  clavePublica;
    }

    @Override
    public BigInteger addCriptograma(Criptograma criptograma) {

        BigInteger cripto =  new BigInteger(String.valueOf(criptograma.getCriptograma()));


        long clavePublica_e = Long.parseLong(cache.buscar("clavePublica_e"));
        long fi_N = Long.parseLong(cache.buscar("fi_N"));
        long clavePublica_n = Long.parseLong(cache.buscar("clavePublica_n"));
        BigInteger e = new BigInteger(String.valueOf(clavePublica_e));
        BigInteger auxi = BigInteger.valueOf(1);

        for (int i = 0; i < fi_N-1; i++) {
            auxi = e.multiply(BigInteger.valueOf(auxi.longValue()));
        }



        BigInteger clavePrivada = auxi.mod(BigInteger.valueOf(clavePublica_n));
         BigInteger aux = BigInteger.valueOf(1);

        for (int i = 0; i < clavePrivada.longValue(); i++) {
        aux = cripto.multiply(BigInteger.valueOf(aux.longValue()));
        }

        return aux.mod(BigInteger.valueOf(clavePublica_n));
    }


    private long mcd(long num1, long num2) {

        long a = Math.max(num1, num2);
        long b = Math.min(num1, num2);

        long resultado = 0;
        do {
            resultado = b;
            b = a % b;
            a = resultado;
        } while (b != 0);

        return resultado;

    }

    @Override
    public List<NumeroPrimo> list() throws NegocioException {

        List<NumeroPrimo> op;
        try {
            op = numerosPrimosDAO.findAll();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new NegocioException(e);
        }

        return op;
    }

    @Override
    public RespuestaGenerica<NumeroPrimo> add(NumeroPrimo numeroPrimo) throws NegocioException {
        MensajeRespuesta m=new MensajeRespuesta();
        try {
            numerosPrimosDAO.save(numeroPrimo);
            m.setMensaje(numeroPrimo.toString());

       //     cache.agregar(numeroPrimo,"clave",3600);
            log.debug(numeroPrimo + "\nGuardado en el cache");
            return new RespuestaGenerica<NumeroPrimo>(numeroPrimo, m);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new NegocioException(e);
        }
    }

    @Override
    public NumeroPrimo update(NumeroPrimo historico) throws NoEncontradoException, NegocioException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public RespuestaGenerica<NumeroPrimo> delete(Long id) throws NegocioException, NoEncontradoException {
        MensajeRespuesta m=new MensajeRespuesta();
        NumeroPrimo h = load(id);
        if(h == null)
            throw new NoEncontradoException("El numero primo que desea eliminar no se encuentra registrado");
        try {
            numerosPrimosDAO.deleteById(id);
            return new RespuestaGenerica<NumeroPrimo>(h, m);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new NegocioException(e);
        }

    }

}
