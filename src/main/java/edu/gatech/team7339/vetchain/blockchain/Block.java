package edu.gatech.team7339.vetchain.blockchain;

import java.util.Date;

public class Block {

    public String hash;
    public String previousHash;
    public BlockUtil block_util = new BlockUtil();
    private String data; //our data will be a simple message.
    private long timeStamp; //as number of milliseconds since 1/1/1970.


    //Block Constructor.
    public Block(String data,String previousHash ) {
        this.data = data;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = block_util.calculateHash(data);
    }

}


