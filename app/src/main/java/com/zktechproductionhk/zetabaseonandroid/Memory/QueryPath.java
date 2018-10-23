package com.zktechproductionhk.zetabaseonandroid.Memory;

public class QueryPath {
    public DataNode parent, self;

    public QueryPath(DataNode parent, DataNode self) {
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
