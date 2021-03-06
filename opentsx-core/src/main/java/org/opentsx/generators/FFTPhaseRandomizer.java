package org.opentsx.generators;

/**
 * Surogat data generator : FFT phase randomisation. 
 * 
 * The idea is simple. A Fourier-Transformation is applied to a measured 
 * real time series. Than, the resulting complex Fourier Coefficients 
 * are modified.
 *  
 * One has to be careful with the frequencies, used in the modification
 * procedure.
 * 
 * C[0] : CONSTANT CONTRIBUTION
 * C[N] : MAX FREQUENCY
 * 
 * C[1] = C[N-1] : FREQUENCY


Fourier Transformed (FT) Surrogates 
-----------------------------------
(also known as Phase-randomized Surrogates)
 
These surrogate series are generated by randomizing the frequency domain phases 
of the original time series. Specifically achieved by Fourier-transforming the 
original series into the frequency domain and then multiplying the phase at each 
frequency by image002ao, where image004ao is a uniformly distributed random 
number in the range image006ao in such a way as to preserve symmetry ...
 
The inverse transform of these realizations are the desired target surrogates.

 * 
 * The Fourier-Transformation is applied again and the
 * result is a random number series with long term 
 * correlations.
 * 
 * The main method is a Tester which shows DFA2 for 
 * multiple test series and a chart with the dependency
 * between alpha and beta, beside the theoretical 
 * curve.
 */

import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.opentsx.chart.simple.MultiChart;
import org.opentsx.data.generator.RNGWrapper;
import org.opentsx.data.exporter.MeasurementTable;
import org.opentsx.data.series.TimeSeriesObject;
import org.opentsx.data.series.TimeSeriesObjectFFT;
import org.opentsx.algorithms.detrending.DetrendingMethodFactory;
import org.opentsx.algorithms.detrending.methods.IDetrendingMethod;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Vector;

public class FFTPhaseRandomizer {
    
    public static final int MODE_shuffle_phase = 0;
    public static final int MODE_multiply_phase_with_random_value = 1;
    
    private static boolean debug = false;

    static double fitMIN = 1.2;
    static double fitMAX = 3.5;
    
    static TimeSeriesObject alphasCALC = new TimeSeriesObject();
    static TimeSeriesObject alphasTHEO = new TimeSeriesObject();
    static Vector<TimeSeriesObject> check = new Vector<TimeSeriesObject>();
    
    static Vector<TimeSeriesObject> tests = null;
    static StringBuffer log = null;
    
    static double[][] alphas = null; 
    static double[] betas = null; 
    
    // nur in dem Fall auch eine DFA Kurve zeigen
    static boolean firstInLoop = false;
    static int numberOfLoop = 0;
    static int numberOfBeta = 0;
 
    static int order = -1; // is defined in method: getRandomRow() ...
    
    
    /**
     *   Parameters with influence on RUNTIME and PERFORMANCE
     */
    static public int nrOfSValues = 250;
    
    static int EXP = 16; // 2 ^ EXP = length of the row

    public static void main(String args[]) throws Exception {
        
        boolean showTest = true;

        // initilize the stdlib.StdRandom-Generator
        RNGWrapper.init();

        // prepare some data structures
        tests = new Vector<TimeSeriesObject>();
        log = new StringBuffer();
                
        log.append("fit range: [" + fitMIN + ", ..., " + fitMAX + " ]\n" );
        
        int Z = 15;
        double STRETCH = 4;

        int LOOPS = 2;
        
        alphas = new double[Z][LOOPS];
        betas = new double[Z]; 

        // double[] alphas = { 0.5, 0.6, 0.7, 0.8, 0.9, 1.0, 1.1, 1.2, 1.3 };  

        int c = 0;
        for( int i=0; i < Z; i++ ) {
           
            if ( showTest ) { 
                System.out.println(">>> i=" +  i + "\n" );
            }
            
            for( int j = 0; j < LOOPS; j++ ) {  
                
                if ( j == 0 ) firstInLoop = true;
                else firstInLoop = false;
                numberOfLoop = j;
                
                TimeSeriesObject measure = getRandomRow( (int)Math.pow(2, EXP) );
                
                TimeSeriesObject mr = getPhaseRandomizedRow( measure, showTest, true, i, MODE_shuffle_phase  );
            
            }    
            
            c = i;
        }
        
        c++;
        
        firstInLoop = true;
        TimeSeriesObject mr2 = getRandomRow( (int)Math.pow(2, EXP) );
        
        TimeSeriesObject mr = getPhaseRandomizedRow( mr2, showTest, true, c, MODE_shuffle_phase );
        
        boolean showLegend = true;
        MultiChart.open(tests, "Fluctuation Function F(s): DFA " + order, 
                        "log(s)", "log(F(s))", false, log.toString(), null );
        
        
        alphasCALC.setLabel("CALC");
        alphasTHEO.setLabel("Theorie");
        // check.add( alphasCALC );
        check.add( alphasTHEO );
        
        calcErrorChart( alphas, numberOfBeta, numberOfLoop, 100.0 );
        
        MultiChart.open(check, "alpha vs. beta", 
                        "beta", "alpha", showLegend );
        
        File f = new File( "./TESTDATA_PhaseRandom" );
        
        MeasurementTable mwt = new MeasurementTable( "random_numbers_CHECK" );
        
        mwt.createParrentFile( f );
        mwt.singleX = false;
        mwt.setMessReihen( check );
        mwt.setHeader("# random_numbers_CHECK");
        mwt.writeToFile();
        
        mwt = new MeasurementTable( "random_numbers_FS" );
        
        mwt.createParrentFile( f );
        mwt.singleX = false;
        mwt.setMessReihen( tests );
        mwt.setHeader("# random_numbers_FS");
        mwt.writeToFile();
        
    }
    
     
    
    /**
     * A random time series of length N with a Gaissian distribution
     * is generated.
     * 
     * see also:   stdlib.StdRandom.gaussian()
     *
     * 
     * @return time series
     * 
     * @throws Exception 
     */
    public static TimeSeriesObject getRandomRow(int N ) throws Exception {
        return TimeSeriesObject.getGaussianDistribution(N);
    }    
        
    /**
     * The procedure creates a surrogate data with the same correlation 
     * properties as the original signal. Following the procedure, one 
     * performs a Fourier transform on the original time series, 
     * preserving the Fourier amplitudes but randomizing the Fourier phases. 
     * 
     * Finally, one performs an inverse Fourier transform to create the real 
     * surrogate data.
     * 
     * A more detailed explanation: 
     *    Podobnik, B., Fu, D. F., Stanley, H. E., & Ivanov, P. C. (2007). 
     *    Power-law autocorrelated stochastic processes with 
     *    long-range cross-correlations. 
     *    The European Physical Journal B, 56(1), 47-52.:
     * 
     * The original paper that describes the method is the following: 
     *    Theiler, J., Eubank, S., Longtin, A., ... (1992). 
     *    Testing for nonlinearity in time series: the method of surrogate data. 
     *    Physica D: Nonlinear Phenomena, 58(1), 77-94. 
     * 
     * As the title suggests, it is a test of nonlinearity in time series. 
     * 
     * The abstract of the paper explains:
     * 
     * The method first specifies some linear process as a null hypothesis, 
     * then generates surrogate data sets which are consistent with this 
     * null hypothesis, and finally computes a discriminating statistic for 
     * the original and for each of the surrogate data sets. If the value 
     * computed for the original data is significantly different than the 
     * ensemble of values computed for the surrogate data, then the null 
     * hypothesis is rejected and nonlinearity is detected.
     *
     * @param showTest
     * @param runDFA
     * @param testZaehler
     * @return
     * @throws Exception 
     */
    public static TimeSeriesObject getPhaseRandomizedRow(TimeSeriesObject ts,
                                                         boolean showTest,
                                                         boolean runDFA,
                                                         int testZaehler,
                                                         int mode ) throws Exception {

        DecimalFormat df = new DecimalFormat("0.000");
        
        int N = ts.yValues.size();
        double[] zr = new double[N];

        // nun wird das Array mit den Daten der ZR übergeben
        TimeSeriesObjectFFT mrNEW = TimeSeriesObjectFFT.convertToMessreiheFFT( ts );
        
        //
        // MANIPULATION OF THE REAL Time Series 
        //
        TimeSeriesObjectFFT temp = null;
        
        switch( mode )  {
          
            case 0 : { 
                
                return mrNEW.getPhaseRandomizedModifiedFFT_SHUFFLED_INV();
                
            }
            case 1 : { 
                
                return mrNEW.getPhaseRandomizedModifiedFFT_MULTIPLY_RANDOM_INV();
            }
            
        };
        
        // debugRows(runDFA, ts, N, temp, testZaehler, showTest, df);

        // System.out.println( temp.yValues.size() + " " + temp.getLabel() );
        return temp;
    }
    
//    /**
//     * The procedure creates a surrogate data with the same correlation properties as the original signal. Following the procedure, one performs a Fourier transform on the original time series, preserving the Fourier amplitudes but randomizing the Fourier phases. Finally, one performs an inverse Fourier transform to create surrogate data.
//     * 
//     * A more detailed explanation from Podobnik, B., Fu, D. F., Stanley, H. E., & Ivanov, P. C. (2007). Power-law autocorrelated stochastic processes with long-range cross-correlations. The European Physical Journal B, 56(1), 47-52.:
//     * 
//     * So, the original paper that describes the method is the following: Theiler, J., Eubank, S., Longtin, A., Galdrikian, B., & Doyne Farmer, J. (1992). Testing for nonlinearity in time series: the method of surrogate data. Physica D: Nonlinear Phenomena, 58(1), 77-94. As the title suggests, it is a test of nonlinearity in time series. As the abstract of the paper explains:
//     *
//     * The method first specifies some linear process as a null hypothesis, then generates surrogate data sets which are consistent with this null hypothesis, and finally computes a discriminating statistic for the original and for each of the surrogate data sets. If the value computed for the original data is significantly different than the ensemble of values computed for the surrogate data, then the null hypothesis is rejected and nonlinearity is detected.
//     * 
//     * @param row
//     * @param showTest
//     * @param runDFA
//     * @param testZaehler
//     * @return
//     * @throws Exception 
//     */
//    public static TimeSeriesObject getPhaseRandomizedRow_SHUFFLE_PHASE(
//            TimeSeriesObject row, boolean showTest, boolean runDFA, int testZaehler ) throws Exception {
//
//        DecimalFormat df = new DecimalFormat("0.000");
//        
//        int N = row.yValues.size();
//
//        double[] zr = new double[N];
//
//        TimeSeriesObjectFFT fftROW = TimeSeriesObjectFFT.convertToMessreiheFFT(row);
//        
//        //
//        // The manipulation is done here ...
//        //
//        TimeSeriesObjectFFT temp = fftROW.getPhaseRandomizedModifiedFFT_SHUFFLED_INV();
//
//        debugRows(runDFA, row, N, temp, testZaehler, showTest, df);
//        
//        // System.out.println( temp.yValues.size() + " " + temp.getLabel() );
//        return temp;
//    }

//    /**
//     * The procedure creates a surrogate data with the same correlation properties as the original signal. Following the procedure, one performs a Fourier transform on the original time series, preserving the Fourier amplitudes but randomizing the Fourier phases. Finally, one performs an inverse Fourier transform to create surrogate data.
//     * 
//     * A more detailed explanation from Podobnik, B., Fu, D. F., Stanley, H. E., & Ivanov, P. C. (2007). Power-law autocorrelated stochastic processes with long-range cross-correlations. The European Physical Journal B, 56(1), 47-52.:
//     * 
//     * So, the original paper that describes the method is the following: Theiler, J., Eubank, S., Longtin, A., Galdrikian, B., & Doyne Farmer, J. (1992). Testing for nonlinearity in time series: the method of surrogate data. Physica D: Nonlinear Phenomena, 58(1), 77-94. As the title suggests, it is a test of nonlinearity in time series. As the abstract of the paper explains:
//
//The method first specifies some linear process as a null hypothesis, then generates surrogate data sets which are consistent with this null hypothesis, and finally computes a discriminating statistic for the original and for each of the surrogate data sets. If the value computed for the original data is significantly different than the ensemble of values computed for the surrogate data, then the null hypothesis is rejected and nonlinearity is detected.
//     * 
//     * @param row
//     * @param showTest
//     * @param runDFA
//     * @param testZaehler
//     * @return
//     * @throws Exception 
//     */
//    public static TimeSeriesObject getPhaseRandomizedRow_MULTIPLY_PHASE_WITH_RANDOM(
//            TimeSeriesObject row, boolean showTest, boolean runDFA, int testZaehler ) throws Exception {
//
//        DecimalFormat df = new DecimalFormat("0.000");
//        
//        int N = row.yValues.size();
//
//        double[] zr = new double[N];
//
//        TimeSeriesObjectFFT fftROW = TimeSeriesObjectFFT.convertToMessreiheFFT(row);
//        
//        TimeSeriesObjectFFT temp = fftROW.getPhaseRandomizedModifiedFFT_MULTIPLY_RANDOM_INV();
//
//        debugRows(runDFA, row, N, temp, testZaehler, showTest, df);
//        
//        return temp;
//    }

    /**
     * inspection of results ...
     * 
     * @param runDFA
     * @param row
     * @param N
     * @param temp
     * @param testZaehler
     * @param showTest
     * @param df
     * @throws Exception 
     */
    private static void debugRows(boolean runDFA, TimeSeriesObject row, int N, TimeSeriesObjectFFT temp, int testZaehler, boolean showTest, DecimalFormat df) throws Exception {
        double[] zr;
        if ( runDFA ) {
            
            Vector<TimeSeriesObject> vr = new Vector<TimeSeriesObject>();
            Vector<TimeSeriesObject> v = new Vector<TimeSeriesObject>();
            vr.add(row );

            zr = row.getData()[1];

            IDetrendingMethod dfa = DetrendingMethodFactory.getDetrendingMethod(DetrendingMethodFactory.DFA2);
            order = dfa.getPara().getGradeOfPolynom();
            dfa.getPara().setzSValues( nrOfSValues );

            // Anzahl der Werte in der Zeitreihe
            dfa.setNrOfValues(N);

            // die Werte für die Fensterbreiten sind zu wählen ...
            //dfa.initIntervalS();
            dfa.initIntervalSlog();
            if ( debug ) dfa.showS();
            
            
            // http://stackoverflow.com/questions/12049407/build-sample-data-for-apache-commons-fast-fourier-transform-algorithm

            dfa.setZR(temp.getData()[1]);

            dfa.calc();

            TimeSeriesObject mr4 = dfa.getResultsMRLogLog();
            mr4.setLabel(row.getLabel() );
            v.add(mr4);

            String status = dfa.getStatus();

            SimpleRegression alphaSR = mr4.linFit(fitMIN, fitMAX);

            double alpha = alphaSR.getSlope();

            alphasCALC.addValuePair( testZaehler, alpha );

            if ( showTest ) {
                MultiChart.open(v, "fluctuation function F(s) [order:" + order + "] ", "log(s)", "log(F(s))", true, "alpha=" + alpha , null);
                if ( firstInLoop ) tests.add(mr4);

                try{
                    alphas[numberOfBeta][numberOfLoop] = alpha;  
                }
                catch(Exception ex) { 
                    
                }
                    
                System.out.println( " alpha = " + df.format( alpha ) );
                System.out.println( "       = " + ( (2 * alpha) - 1.0 ) );

            }

        }
    }

    
    static TimeSeriesObject mrAV = new TimeSeriesObject();  // Mittelwerte
    static TimeSeriesObject mrSTD = new TimeSeriesObject();  // standardabweichung
    static TimeSeriesObject mrMAX = new TimeSeriesObject();  // ERROR
    static TimeSeriesObject mrMIN = new TimeSeriesObject();  // ERROR
        
    /**
     * some more debugging code ...
     * 
     * @param d
     * @param nrB
     * @param nrLOOPS 
     */
    private static void calcErrorChart(double[][] d, int nrB, int nrLOOPS, double scaleSTDDEV ) {
        
        mrAV = new TimeSeriesObject();  // Mittelwerte
        mrSTD = new TimeSeriesObject();  // standardabweichung
        mrMAX = new TimeSeriesObject();  // ERROR
        mrMIN = new TimeSeriesObject();  // ERROR
    
        mrAV.setLabel("mean");
        mrSTD.setLabel("stddev * " + scaleSTDDEV);
        mrMAX.setLabel("upper");
        mrMIN.setLabel("lower");
        
        for( int iB = 0; iB < nrB; iB++ ) { 
            
            double[] L = new double[nrLOOPS];
            
            // werte umlagern
            for( int iL = 0; iL < nrLOOPS; iL++ ) { 
                L[iL] = d[iB][iL];
            }
            try {

                StandardDeviation stdev = new StandardDeviation();

                double mean = StatUtils.mean(L);
                double std = stdev.evaluate(L);

                mrAV.addValuePair(betas[iB] , mean );
                mrSTD.addValuePair(betas[iB] , std * scaleSTDDEV);

                mrMAX.addValuePair( betas[iB] , mean+std );
                mrMIN.addValuePair( betas[iB] , mean-std );

            }
            catch(Exception ex) {

                System.err.println( ex.getMessage() );
                System.err.println( ex.getCause() );
                
            }
        }
        
        check.add( mrAV );        
        check.add( mrSTD );
        check.add( mrMAX );
        check.add( mrMIN );
        
    }
    

}
