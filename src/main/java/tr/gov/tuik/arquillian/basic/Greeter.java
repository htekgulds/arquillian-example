package tr.gov.tuik.arquillian.basic;

import javax.inject.Inject;
import java.io.PrintStream;

/**
 * Created by studentA on 7/27/2016.
 */
public class Greeter {

    @Inject
    private PhraseBuilder phraseBuilder;

    public void greet(PrintStream to, String name) {
        to.println(createGreeting(name));
    }

    public String createGreeting(String name) {
        return phraseBuilder.buildPhrase("hello", name);
    }
}
