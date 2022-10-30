package ar.edu.iua.segInfo.negocio.service;

import ar.edu.iua.segInfo.modelo.NumeroPrimo;
import ar.edu.iua.segInfo.negocio.excepciones.NegocioException;
import ar.edu.iua.segInfo.negocio.excepciones.NoEncontradoException;
import ar.edu.iua.segInfo.util.RespuestaGenerica;

import java.util.List;

public interface INumeroPrimoBusiness {
    NumeroPrimo load(Long id) throws NoEncontradoException, NegocioException;

    List<NumeroPrimo> list() throws NegocioException;

    RespuestaGenerica<NumeroPrimo> add(NumeroPrimo numeroPrimo) throws NegocioException;

    NumeroPrimo update(NumeroPrimo historico) throws NoEncontradoException, NegocioException;

    RespuestaGenerica<NumeroPrimo> delete(Long id) throws NegocioException, NoEncontradoException;
}
