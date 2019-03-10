package simulation;
/*
 * Copyright (c) 2017, Mikl&oacute;s Cs&#369;r&ouml;s
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

import java.util.Arrays;
import java.util.Random;

/**
 *
 * Gompertz-Makeham distribution for lifespan.
 * 
 * Parametrized by accident, death rate and scale.
 *
 * @author Mikl&oacute;s Cs&#369;r&ouml;s
 */

//Miklos Csuros is the author of this code.

/*This class offers multiple methods
    that determine random values such
    as average death time of a person,
    average waiting time before a woman
    conceives a baby...etc
 */
public class AgeModel 
{
    private final double death_rate;
    private final double accident_rate;
    private final double age_factor;
    
    private static final double DEFAULT_ACCIDENT_RATE = 0.01; // 1% chance of dying per year
    private static final double DEFAULT_DEATH_RATE = 12.5;
    private static final double DEFAULT_SCALE = 100.0; // "maximum" age [with death rate 1]
    
    public AgeModel(double accident_rate, double death_rate, double age_scale)
    {
        this.death_rate = death_rate;
        this.age_factor = Math.exp(age_scale/death_rate);
        this.accident_rate = accident_rate;
        
    }
    
    /**
     * Instantiation with default values (human).
     */
    public AgeModel() {
        this(DEFAULT_ACCIDENT_RATE, DEFAULT_DEATH_RATE, DEFAULT_SCALE);
    }
    
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(getClass().getName());
        sb.append("[acc ").append(accident_rate).append(", age ").append(death_rate).append(", agefactor ").append(age_factor);
        sb.append("]");
        return sb.toString();
    }
    
    /**
     * Probability of surviving past the given age
     * 
     * @param age 
     * @return probability of dying after the given age
     */
    public double getSurvival(double age)
    {
       return Math.exp(-accident_rate*age -death_rate*Math.expm1(age/death_rate)/age_factor);
    }
    
    /**
     * Expected time span (TS) for mating: average number of children will be TS/matingrate.
     * 
     * @param min_age minimum age of sexual maturity
     * @param max_age maximum age of parenting
     * @return 
     */
    public double expectedParenthoodSpan(double min_age, double max_age)
    {
        
        // integration of the survival function over the mating age
        
        // numerical integration with simpson's rule, dynamic setting of resolution
        
        int n = 1; // number of intervals along the range
        double d = (max_age-min_age)/n;
        
        double st = d*0.5*(getSurvival(min_age)+getSurvival(max_age)); 
        
        
        double espan = 0.0;
        double old_espan = -1.0; // does not matter much
        
        for (int iter=1; iter<20;iter++)
        {
            double x0=min_age+d*0.5;
            double s2=0.0;
            for (int i=0;i<n;i++)
            {
                double x = x0+i*d;
                s2 += getSurvival(x);
            }
            double old_st = st;
            st = 0.5*(st+d*s2); // simple trapezoidal 
            espan = (4.0*st-old_st)/3.0; // Simpson's ... better than st

            n = n*2;
            d=d*0.5;
            
            if (iter>5 // first five iteration kept 
                    && (Math.abs(old_espan-espan)<1e-7*old_espan
                    || (espan==0.0 && old_espan==0.0) ))
                break;
            old_espan = espan;
        }         
        return espan;
    }
    
    /**
     * Exponentially distributed random variable.
     * 
     * 
     * @param RND random number generator
     * @param rate inverse of the mean
     * @return Exponential(rate)
     */
    public static double randomWaitingTime(Random RND, double rate)
    {
        return  -Math.log(RND.nextDouble())/rate;
    }

    /**
     * A random value with the specified lifespan distribution.
     * 
     * @param RND Psudorandom number generator for uniform[0,1]
     * 
     * @return a random value distributed by Gomperz-Makeham
     */
    public double randomAge(Random RND)
    {
        // psudorandom by exponential for accident-related death
        double accidental_death = -Math.log(RND.nextDouble())/accident_rate;
        // pseudorandom by Gompertz for old-age
        double u = RND.nextDouble();
        double age_death = death_rate*Math.log1p(-Math.log(u)/death_rate*age_factor);
        
        return Math.min(age_death, accidental_death);
    }
    
    /**
     * Test for tabulating random lifespans from command line.
     * 
     * @param args accident-rate death-rate [scale]
     */
    public static void main(String[] args)
    {
        int arg_idx = 0;
        double acc = Double.parseDouble(args[arg_idx++]);
        double dth = Double.parseDouble(args[arg_idx++]);
        double scale = DEFAULT_SCALE;
        
        if (arg_idx<args.length)
            scale = Double.parseDouble(args[arg_idx++]);
        
        AgeModel M= new AgeModel(acc, dth, scale);

        Random RND = new Random();
        
        int smp_size = 1000; // this many random values
        
        double[] lifespan = new double[smp_size];
        
        double avg = 0.0;
        for (int r=0; r<smp_size; r++)
        {
            double d = M.randomAge(RND);
            avg += d;
            lifespan[r] = d;
        }
        avg /= smp_size;
        Arrays.sort(lifespan);
        
        // plot for distrubution function - 1st and 3rd columns should match (empirical vs. theoretical cumulative distribution function)
        for (int r = 0; r<smp_size; r++)
        {
            System.out.println((1+r)+"\t"+lifespan[r]+"\t"+smp_size*(1.0-M.getSurvival(lifespan[r])));
        }
        double span = M.expectedParenthoodSpan(AgeModelSim.MIN_MATING_AGE_F, AgeModelSim.MAX_MATING_AGE_F);
        double stable_rate = 2.0/span;
        System.out.println("avg\t"+avg+"\tmating span(mother): "+span+"\tstable "+stable_rate+"\t// 1/"+span/2.0);
    }
    
}