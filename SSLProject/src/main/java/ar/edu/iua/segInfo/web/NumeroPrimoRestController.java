package ar.edu.iua.segInfo.web;

import ar.edu.iua.segInfo.modelo.NumeroPrimo;
import ar.edu.iua.segInfo.modelo.dto.ClavePublicaDTO;
import ar.edu.iua.segInfo.modelo.dto.Criptograma;
import ar.edu.iua.segInfo.negocio.excepciones.NegocioException;
import ar.edu.iua.segInfo.negocio.excepciones.NoEncontradoException;
import ar.edu.iua.segInfo.negocio.service.INumeroPrimoBusiness;
import ar.edu.iua.segInfo.util.MensajeRespuesta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/numeroPrimo")
public class NumeroPrimoRestController {


    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private INumeroPrimoBusiness numeroPrimoBusiness;




    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<NumeroPrimo> load(@PathVariable("id") Long id) {
        try {

            return new ResponseEntity<NumeroPrimo>(numeroPrimoBusiness.load(id), HttpStatus.OK);
        } catch (NegocioException e) {
            return new ResponseEntity<NumeroPrimo>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NoEncontradoException e) {
            // TODO Auto-generated catch block
            return new ResponseEntity<NumeroPrimo>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/publicKey", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ClavePublicaDTO> publicKey() {
        try {

            return new ResponseEntity<ClavePublicaDTO>(numeroPrimoBusiness.publicKey(), HttpStatus.OK);
        } catch (NegocioException e) {
            return new ResponseEntity<ClavePublicaDTO>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NoEncontradoException e) {
            // TODO Auto-generated catch block
            return new ResponseEntity<ClavePublicaDTO>(HttpStatus.NOT_FOUND);
        }
    }


    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<NumeroPrimo>> listAll() {
        try {
            return new ResponseEntity<List<NumeroPrimo>>(numeroPrimoBusiness.list(), HttpStatus.OK);
        } catch (NegocioException e) {
            return new ResponseEntity<List<NumeroPrimo>>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/criptograma", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addCriptograma(@RequestBody Criptograma criptograma) {
        return new ResponseEntity<String>(numeroPrimoBusiness.addCriptograma(criptograma), HttpStatus.OK);
    }



    //---------Guardar Historico en BD------------------

    @PostMapping(value = "/add", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MensajeRespuesta> add(@RequestBody NumeroPrimo numeroPrimo) {
        try {
            MensajeRespuesta r =   numeroPrimoBusiness.add(numeroPrimo).getMensaje();;
            return new ResponseEntity<MensajeRespuesta>(r, HttpStatus.CREATED);
        } catch (NegocioException e) {
            log.error(e.getMessage(), e);
            MensajeRespuesta r=new MensajeRespuesta(-1,e.getMessage());
            return new ResponseEntity<MensajeRespuesta>(r,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //---------Ultimo valor ------------------


    @DeleteMapping(value="/{id}")
    public ResponseEntity<MensajeRespuesta> eliminar(@PathVariable("id") Long id) {
        try {
            MensajeRespuesta r =   numeroPrimoBusiness.delete(id).getMensaje();
            return new ResponseEntity<MensajeRespuesta>(r,HttpStatus.OK);
        } catch (NegocioException e) {
            log.error(e.getMessage(), e);
            MensajeRespuesta r=new MensajeRespuesta(-1,e.getMessage());
            return new ResponseEntity<MensajeRespuesta>(r,HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NoEncontradoException e) {
            log.error(e.getMessage(), e);
            MensajeRespuesta r=new MensajeRespuesta(-1,e.getMessage());
            return new ResponseEntity<MensajeRespuesta>(r,HttpStatus.NOT_FOUND);
        }
    }
}
