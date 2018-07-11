/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import java.util.Iterator;
import java.util.LinkedList;

/**
 *
 * @author SONPH
 */
public class Words implements Comparable<Words> {

    private String word;
    private LinkedList<String> meanList;

    public Words() {
        this.word = "";
        this.meanList = new LinkedList<>();
    }

    public Words(String word) {
        this();
        this.word = word;
    }

    public Words(String word, String mean) {
        this();
        this.word = word;
        this.meanList.add(mean);
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setMean(String mean) {
        this.meanList.add(mean);
    }

    public String getMean() {
        String result = "";
        int count = 0;
        for (String e : meanList) {
            result += e;
            count++;
            if (count < meanList.size()) {
                result += ", ";
            }
        }
        return result;
    }

    public LinkedList<String> getMeanlist() {
        return this.meanList;
    }

    @Override
    public String toString() {
        return word + ": " + getMean();
    }

    @Override
    public int compareTo(Words other) {
        return this.word.toLowerCase().compareTo(other.getWord().toLowerCase());
    }

}
