# restaurant-puzzle

This project is meant to solve a little word scramble puzzle. The puzzle gives a list of words, which should be reordered such that they sound like some other coherent collection of words. For example, "TAB ESS MEANT LISH" might be rearranged to sound like "ESTABLISHMENT." Due to the strict adherence to the phoneme dictionary, however..the program won't currently make that link.

## Usage

Only looks at one line of clue words. Reads in a phoneme dictionary and the first line of the clue file, and write the answers to a file. Prunes down to answers with at least `b` of the base words (or their homophones) missing. 

Run with `lein run -- `, followed by options.

  | Option | Long Option | Default | Description |
  | ------| ------| ------| ------|
  |-d | --dictionary DICTIONARY FILE | resources/bland-vowel-dict | Dictionary file location  
  |-c | --clues CLUE FILE            | resources/clue_words.txt   | Clue file location  
  |-a | --answers ANSWER FILE        | resources/answers.txt     |  File to write answers to (overwrite)  
  |-b | --base-words BASE WORDS      | 1                        |   Allow all but b base words in answers  
  |-h | --help                        | nil                       |   Display usage information.  


## License

Copyright © 2021 theChad

This program and the accompanying materials are made available under the
terms of the Eclipse Public License 2.0 which is available at
http://www.eclipse.org/legal/epl-2.0.

This Source Code may also be made available under the following Secondary
Licenses when the conditions for such availability set forth in the Eclipse
Public License, v. 2.0 are satisfied: GNU General Public License as published by
the Free Software Foundation, either version 2 of the License, or (at your
option) any later version, with the GNU Classpath Exception which is available
at https://www.gnu.org/software/classpath/license.html.
