package edu.gatech.team7339.vetchain.blockchain;

import java.io.Serializable;
import java.util.LinkedList;

public class BlockUtil implements Serializable {
    public BlockUtil blockUtil;

    public String calculateHash(String data) {
        String calculatedhash = StringUtil.applySha256(
                // previousHash +
                // Long.toString(timeStamp) +
                data
        );
        return calculatedhash;
    }

    /*
    public static Boolean isChainValid(LinkedList<Block> blockchain) {
        Block currentBlock;
        Block previousBlock;


        //loop through blockchain to check hashes:
        for(int i=1; i < blockchain.size(); i++) {
            currentBlock = blockchain.get(i);
            previousBlock = blockchain.get(i-1);
            //compare registered hash and calculated hash:
            if(!currentBlock.hash.equals(currentBlock.calculateHash()) ){
                System.out.println("Current Hashes not equal");
                return false;
            }
            //compare previous hash and registered previous hash
            if(!previousBlock.hash.equals(currentBlock.previousHash) ) {
                System.out.println("Previous Hashes not equal");
                return false;
            }
        }
        return true;
    }
    */
}
