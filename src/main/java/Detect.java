import com.google.common.base.Optional;
import com.google.common.io.Files;
import com.optimaize.langdetect.LanguageDetector;
import com.optimaize.langdetect.LanguageDetectorBuilder;
import com.optimaize.langdetect.i18n.LdLocale;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfile;
import com.optimaize.langdetect.profiles.LanguageProfileReader;
import com.optimaize.langdetect.text.CommonTextObjectFactories;
import com.optimaize.langdetect.text.TextObject;
import com.optimaize.langdetect.text.TextObjectFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Detect {

    public File getResource(final String filename) {
        return new File(Objects.requireNonNull(getClass().getClassLoader().getResource(filename)).getFile());
    }

    public List<String> readFilesIntoList(File file) {
        List<String> copyList = Collections.emptyList();
        try {
            copyList = Files.readLines(file, Charset.defaultCharset());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return copyList;
    }

    public String scanLanguageWeb(StringBuilder text) {
        Optional<LdLocale> lang = null;
        try {
            List<LanguageProfile> languageProfiles = new LanguageProfileReader().readAllBuiltIn();
            LanguageDetector languageDetector = LanguageDetectorBuilder.create(NgramExtractors.standard())
                    .withProfiles(languageProfiles)
                    .build();
            TextObjectFactory textObjectFactory = CommonTextObjectFactories.forDetectingOnLargeText();
            lang = languageDetector.detect(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lang.get().getLanguage();
    }

    public String scanLanguage(String fileName) {
        Optional<LdLocale> lang = null;
        try {
            List<LanguageProfile> languageProfiles = new LanguageProfileReader().readAllBuiltIn();
            LanguageDetector languageDetector = LanguageDetectorBuilder.create(NgramExtractors.standard())
                    .withProfiles(languageProfiles)
                    .build();
            TextObjectFactory textObjectFactory = CommonTextObjectFactories.forDetectingOnLargeText();
            List<String> myList = readFilesIntoList((getResource(fileName)));
            StringBuilder text = new StringBuilder();
            for (String word : myList) {
                text.append(word);
            }
            lang = languageDetector.detect(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lang.get().getLanguage();
    }

    public StringBuilder scanWebPageText(String URL) {
        StringBuilder text = new StringBuilder();
        try {
            Document doc = Jsoup.connect(URL).get();
            Elements element = doc.select("p");
            for (Element t : element) {
                text.append(t);
            }
        } catch (IOException ex) {
            Logger.getLogger(Detect.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return text;
    }
}
