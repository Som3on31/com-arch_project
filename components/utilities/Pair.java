package components.utilities;

public class Pair<F, S> {
    F fst;
    S snd;

    public Pair(F first, S second) {
        fst = first;
        snd = second;
    }

    public F fst() {
        return fst;
    }

    public S snd() {
        return snd;
    }
}
