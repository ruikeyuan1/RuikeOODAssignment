import nltk  # This is a major NLP library

"""
This file generates the abc_corpus.txt file.
Do not use it if it is already included in the zip.

However, if you think you can parse the corpus better you can change the code below.
Parsing the corpus better means that when creating sentence less nonsense word are used.
"""

# download corpus
nltk.download('punkt')
from nltk.corpus import abc

i = 0
sentences = []
for s in abc.sents():
    sentence = []
    apostrophe_flag = False
    for w in s:
        w2 = "".join([l for l in w.lower() if l.isalpha() or l == "."])
        if w == "'":
            apostrophe_flag = True
        elif apostrophe_flag and w == "t":
            sentence[-1] = sentence[-1][:-1]
            sentence.append("not")
        elif len(w2) > 1:
            sentence.append(w2)
    sentence = " ".join(sentence) + "."
    sentences.append(sentence)

with open("text.txt", 'w') as f:
    f.write("\n".join(sentences))
