package app.bs.polonif.model;

import java.util.ArrayList;
import java.util.List;

public class TaskModel {
    String id;
    String titulo, descricao, imagem;
    List<DecorateModel> decorates = new ArrayList<>();

    public TaskModel() {
    }

    public TaskModel(String titulo, String descricao, String imagem, List<DecorateModel> decorates) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.imagem = imagem;
        this.decorates = decorates;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public List<DecorateModel> getDecorates() {
        return decorates;
    }

    public void setDecorates(List<DecorateModel> decorates) {
        this.decorates = decorates;
    }
}
