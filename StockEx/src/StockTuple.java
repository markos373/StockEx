public class StockTuple<K, L, M, N>{

    private K val1;
    private L val2;
    private M val3;
    private N val4;

    public StockTuple(K val1, L val2, M val3, N val4){
        this.val1 = val1;
        this.val2 = val2;
        this.val3 = val3;
        this.val4 = val4;
    }

    public K getVal1(){
        return this.val1;
    }

    public L getVal2(){
        return this.val2;
    }

    public M getVal3(){
        return this.val3;
    }

    public N getVal4(){
        return this.val4;
    }

}
