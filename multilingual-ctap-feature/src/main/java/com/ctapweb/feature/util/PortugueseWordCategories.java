package com.ctapweb.feature.util;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Definition of word categories for Portuguese
 * @author zweiss, edemattos
 *
 * Universal Dependencies: https://universaldependencies.org/u/pos/all.html
 * Portuguese Bosque: https://universaldependencies.org/treebanks/pt_bosque/index.html#pos-tags
 *
 */
public class PortugueseWordCategories extends UDWordCategories {

    String[] mesoInfl; // mesoclitics only attach to specific verb tenses

    public PortugueseWordCategories() {
        super();

        relativePronouns = new String[] {
                "que", "quem", "onde", "qual", "quais", "cujo", "cuja",
                "cujos", "cujas", "quanto", "quanta", "quantos", "quantas"
        };

        interrogativePronouns = new String[] {
                "que", "quem", "qual", "quais", "quanto", "quanta", "quantos", "quantas"
        };

        possessivePronouns = new String[] {
                "meu", "minha", "meus", "minhas",
                "teu", "tua", "teus", "tuas",
                "seu", "sua", "seus", "suas",
                "dele", "dela", "deles", "delas",
                "nosso", "nossa", "nossos", "nossas",
                "vosso", "vossa", "vossos", "vossas"
        };

        articleForms = new String[] {
                "o", "a", "os", "as",
                "um", "uma", "uns", "umas"
        };

        cliticForms = new String[] {
                "me", "te", "se", "si",
                "nos", "vos", "lhe", "lhes",
                "o", "lo", "no", "os", "los", "nos",
                "a", "la", "na", "as", "las", "nas",
        };

        mesoInfl = new String[] {
                "ei", "ás", "á", "emos", "eis", "ão", // future indicative
                "ia", "íamos", "iam" // conditional
        };
    }

    @Override
    public boolean isClitic(String form, String tag, String headPOS, String[] feats) {

        if (!tag.equals("PRON") || !isVerb(headPOS)) { return false; } // Portuguese clitics are always hosted by verb

        for (String s : feats) {
            String feat = s.split("=")[0];
            String val = s.split("=")[1];
            if (feat.equals("PronType") && !val.equals("Prs")) { return false; } // exclude demonstratives: "[o] que"
            if (feat.equals("Case") && !(val.equals("Acc") || val.equals("Dat"))) { return false; }
        }

        for (String token : cliticForms) {
            if (token.equalsIgnoreCase(form)) {
                return true;
            }
        }
        return false;
    }

    // Stanza tokenization for mesoclitics hardly works, ex. dar-te-lo-ei "(I) will give you it"
    // surface form may not be split, so clitics often remain attached to host and verb may not be identified/tagged
    public boolean isMesoclitic(String form) {
        // pattern: verb-[optional 2nd clitic]-(required clitic)-(inflection)
        Pattern p = Pattern.compile("\\w+-(?:\\w{1,4}-)?(\\w{1,4})-(\\w{1,5})", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(form);
        return m.find()
                && (Arrays.asList(cliticForms).contains(m.group(1)))
                && (Arrays.asList(mesoInfl).contains(m.group(2)));
    }
}
