import javax.swing.*                                                                                ;
import java.io.*                                                                                    ;
import java.nio.file.*                                                                              ;
import java.util.*                                                                                  ;
import java.util.regex.*                                                                            ;

public class Main                                                                                   {

    private static final int WALL_COLUMN = 100                                                      ;
    private static final Pattern TRAILING_SYMBOLS = Pattern.compile("([\\s]*)([\\);}{)(]*)$"        );

    public static void main(String[] args) throws IOException                                       {
        JFileChooser chooser = new JFileChooser                                                     ();
        chooser.setDialogTitle("Select Folder to Align"                                             );
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY                                  );
        chooser.setAcceptAllFileFilterUsed(false                                                    );

        if (chooser.showOpenDialog(null) != JFileChooser.APPROVE_OPTION)                            {
            System.out.println("No folder selected. The Java gods are spared... for now."           );
            return                                                                                  ;
                                                                                                    }

        Path root = chooser.getSelectedFile().toPath                                                ();
        Path scoliosisRoot = root.resolve("SyntaxScoliosis"                                         );

        Files.walk(root                                                                             )
                .filter(p -> p.toString().endsWith(".java"                                          ))
                .forEach(p -> adjustFile(p, root, scoliosisRoot                                     ));

        System.out.println("\nAll files have been adjusted inside: " + scoliosisRoot                );
                                                                                                    }

    private static void adjustFile(Path filePath, Path root, Path scoliosisRoot)                    {
        try                                                                                         {
            Path relativePath = root.relativize(filePath                                            );
            Path outPath = scoliosisRoot.resolve(relativePath                                       );

            Files.createDirectories(outPath.getParent                                               ());

            List<String> lines = Files.readAllLines(filePath                                        );
            List<String> adjustedLines = new ArrayList<>                                            ();

            for (String line : lines)                                                               {
                adjustedLines.add(adjustLine(line                                                   ));
                                                                                                    }

            Files.write(outPath, adjustedLines                                                      );
            System.out.println("Aligned: " + relativePath                                           );

        } catch (IOException e)                                                                     {
            e.printStackTrace                                                                       ();
                                                                                                    }
                                                                                                    }

    private static String adjustLine(String line)                                                   {
        Matcher m = TRAILING_SYMBOLS.matcher(line                                                   );
        if (!m.find()) return line                                                                  ;

        String trailing = m.group(2                                                                 );
        if (trailing.isEmpty()) return line                                                         ;

        String before = line.substring(0, m.start(2                                                 ));
        int len = before.length                                                                     ();

        if (len < WALL_COLUMN)                                                                      {
            int pad = WALL_COLUMN - len                                                             ;
            return before + " ".repeat(pad) + trailing                                              ;
                                                                                                    }
        return line                                                                                 ;
                                                                                                    }
                                                                                                    }
