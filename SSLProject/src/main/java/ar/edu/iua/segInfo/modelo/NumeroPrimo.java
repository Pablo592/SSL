package ar.edu.iua.segInfo.modelo;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "primos")
public class NumeroPrimo implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long iprimo;

    @Column(name="primoN")
    private Long numero;


    public Long getIprimo() {
        return iprimo;
    }

    public void setIprimo(Long iprimo) {
        this.iprimo = iprimo;
    }

    public Long getNumero() {
        return numero;
    }

    public void setNumero(Long numero) {
        this.numero = numero;
    }

    @Override
    public String toString() {
        return "NumeroPrimo{" +
                "iprimo=" + iprimo +
                ", numero=" + numero +
                '}';
    }

}
