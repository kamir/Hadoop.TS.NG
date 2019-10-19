package org.opentsx.tsbucket;

import org.opentsx.data.series.TimeSeriesObject;
import org.opentsx.core.SingleRowTSO;
import org.opentsx.core.TSBucket;
import org.opentsx.core.TSOperation;
import java.io.IOException;
import java.util.Vector;

/**
 * A TSBucket is the date container for managing a set of time series.
 * 
 * The BucketLoader knows how to read data from disk or DB.
 * 
 * @author kamir
 */

public class BucketLoader {
    
    TSBucket b = null;

    public TSBucket getTSBucket() {
        return b;
    }

    public Vector<Integer> getIds() {
        return ids;
    }
    
    boolean DO_FILTER = true;
    Vector<Integer> ids = null;
    
    public static int default_LIMIT = 50;
    public static boolean default_INMEM = true;
    
    /**
     * load bucket and store it in a variable called : b
     * 
     * @param fn
     * @throws IOException 
     */
    public void loadBucketData( String fn ) throws IOException {
                
        TSBucket bucket = TSBucket.createEmptyBucketFull();
        bucket.inMEM = default_INMEM;
        
        // no ID-vector, no TSOperation
        bucket.loadFromSequenceFile( fn, null, null );
        
        b = bucket;
    }  
    
    /**
     * Load and get the TSBucket.
     * 
     * @param fn
     * @return
     * @throws IOException 
     */
    public TSBucket loadBucket( String fn ) throws IOException {
        
        TSBucket bucket = TSBucket.createEmptyBucketFull();
        bucket.inMEM = default_INMEM;
        
        bucket.loadFromSequenceFile( fn, ids, null );
        
        b = bucket;
        
        return b;
    }  
    
    
    /**
     * Load and preprocess the data.
     * The result bucket contains the preprocessed data.
     * 
     * @param fn
     * @param tst
     * @return
     * @throws IOException 
     */
    public TSBucket _processBucket( String fn, TSOperation tst ) throws IOException {

        TSBucket bucket = TSBucket.createEmptyBucketFull();
        
        bucket.isProcessed = true;
        
        // WE SKIP THE HISTORY HERE
        // bucket.isProcessedBy = tst.getClass();

        bucket._loadAndProcess( fn, tst, ids );
        
        // store the preprocessed bucket also localy...
        
        b = bucket;
        return bucket;
    }  

    // get the bucketdata ...
    public Vector<TimeSeriesObject> getBucketData() {
      return b.getBucketData();
    }

    public void loadBucket(String absolutePath, Vector<Integer> i) throws IOException {
        if ( i == null ) {
            loadBucket( absolutePath );
        }
        else {
            DO_FILTER = true;
            ids = i;
            loadBucket( absolutePath );
        } 
    }

    public void _processBucket(String resultPath, SingleRowTSO singleTsTool, Vector<Integer> i) throws IOException {
        if ( i == null ) {
            _processBucket( resultPath, singleTsTool  );
        }
        else {
            DO_FILTER = true;
            ids = i;
            _processBucket( resultPath, singleTsTool );
        } 
    }

    public void setLimit(int i) {
       default_LIMIT = i;
       TSBucket.default_LIMIT = i;
    }
    
    
            
    
}
