package transport.core;

public interface Suspendable {
    void suspendre();
    void reactiver();
    boolean estSuspendu();
    String getEtat();
}

