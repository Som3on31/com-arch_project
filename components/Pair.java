package components;

public class Pair<F, S> {
    private F fst;
    private S snd;

    public Pair(F f, S s) {
        fst = f;
        snd = s;
    }

    /**
     * Returns soft copy data of {@code fst}
     * 
     * @return {@code fst}
     */
    public F fst() {
        return fst;
    }

    /**
     * Returns soft copy data of {@code snd}
     * 
     * @return {@code snd}
     */
    public S snd() {
        return snd;
    }
}
