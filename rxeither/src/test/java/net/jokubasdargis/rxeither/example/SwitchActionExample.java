package net.jokubasdargis.rxeither.example;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import net.jokubasdargis.rxeither.Either;
import rx.functions.Action1;

public class SwitchActionExample {

    public static void main(String[] args) {
        System.out.println("Type Either a string or an Int: ");

        String in = "";
        Either<String, Integer> either;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            in = reader.readLine();
            either = Either.right(Integer.parseInt(in));
        } catch (NumberFormatException | IOException e) {
            either = Either.left(in);
        }

        either.continued(new Action1<String>() {
            @Override
            public void call(String s) {
                System.out.println("You passed me the String: " + s);
            }
        }, new Action1<Integer>() {
            @Override
            public void call(Integer x) {
                System.out.println(
                        "You passed me the Int: " + x
                                + ", which I will increment. " + x + " + 1 = "
                                + (x + 1));
            }
        });
    }

    private SwitchActionExample() {
        throw new AssertionError("No instances");
    }
}
