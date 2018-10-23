package com.zktechproductionhk.zetabaseonandroid.Memory;

public class QueryPath {
    public Node parent, self;

    public QueryPath(Node parent, Node self) {
        this.parent = parent;
        this.self = self;
    }

    @Override
    public String toString() {
        return "QueryPath{" +
                "parent=" + parent +
                ", self=" + self +
                '}';
    }
}
