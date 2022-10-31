package ar.edu.iua.segInfo.modelo.dto;

public class ClavePublicaDTO {

    public ClavePublicaDTO(long e, long n){
        this.e = e;
        this.n = n;
    }
    private long n;
    private long e;

    public long getN() {
        return n;
    }

    public void setN(long n) {
        this.n = n;
    }

    public long getE() {
        return e;
    }

    public void setE(long e) {
        this.e = e;
    }
}
