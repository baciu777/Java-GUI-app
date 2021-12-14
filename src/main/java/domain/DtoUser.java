package domain;

public class DtoUser {
    private String name;
    private String relation;
    public DtoUser(String name, String relation)
    {
        this.name = name;
        this.relation = relation;
    }
    public String getName()
    {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getRelation() {
        return relation;
    }
}

