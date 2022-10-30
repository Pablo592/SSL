package ar.edu.iua.segInfo.negocio.service;

import ar.edu.iua.segInfo.modelo.NumeroPrimo;
import ar.edu.iua.segInfo.modelo.cache.Memcache;
import ar.edu.iua.segInfo.modelo.repository.NumerosPrimosRepository;
import ar.edu.iua.segInfo.negocio.excepciones.NegocioException;
import ar.edu.iua.segInfo.negocio.excepciones.NoEncontradoException;
import ar.edu.iua.segInfo.util.MensajeRespuesta;
import ar.edu.iua.segInfo.util.RespuestaGenerica;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

            cache.agregar(numeroPrimo,"clave",3600);
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
