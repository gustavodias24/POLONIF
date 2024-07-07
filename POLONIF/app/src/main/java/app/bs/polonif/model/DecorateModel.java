package app.bs.polonif.model;

import java.util.ArrayList;
import java.util.List;

public class DecorateModel {
    String imagem, pergunta, respErrada1, respErrada2, respCorreta;

    public DecorateModel() {
    }

    public DecorateModel(String imagem, String pergunta, String respErrada1, String respErrada2, String respCorreta) {
        this.imagem = imagem;
        this.pergunta = pergunta;
        this.respErrada1 = respErrada1;
        this.respErrada2 = respErrada2;
        this.respCorreta = respCorreta;
    }

    @Override
    public String toString() {
        return
                "Pergunta: " + pergunta + '\n' +
                "RespErrada1: " + respErrada1 + '\n' +
                "RespErrada2: " + respErrada2 + '\n' +
                "RespCorreta: " + respCorreta ;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getPergunta() {
        return pergunta;
    }

    public void setPergunta(String pergunta) {
        this.pergunta = pergunta;
    }

    public String getRespErrada1() {
        return respErrada1;
    }

    public void setRespErrada1(String respErrada1) {
        this.respErrada1 = respErrada1;
    }

    public String getRespErrada2() {
        return respErrada2;
    }

    public void setRespErrada2(String respErrada2) {
        this.respErrada2 = respErrada2;
    }

    public String getRespCorreta() {
        return respCorreta;
    }

    public void setRespCorreta(String respCorreta) {
        this.respCorreta = respCorreta;
    }
}
