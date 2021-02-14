package ru.mai.dep810;

public class Vertex {

    private String _name;

    public Vertex(String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return _name.equals(vertex._name);
    }
}
