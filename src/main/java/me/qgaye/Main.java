package me.qgaye;

public class Main {
    public static void main(String[] args) {
        Pub.just(1, 2, 3, 4, 5, 6, 7, 8, 9)
                .map(o -> o * o)
                .map(Math::sqrt)
                .subscribe(new Sub<>());
    }
}
