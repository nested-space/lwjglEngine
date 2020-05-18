package com.edenrump.math.arrays;

import org.lwjgl.system.MemoryStack;
import java.nio.FloatBuffer;

import org.testng.annotations.Test;
import org.testng.Assert;

public class Vector2fTest {

    Vector2f one = new Vector2f(1, 1);
    Vector2f pos_neg = new Vector2f(123, -425);
    Vector2f neg_neg = new Vector2f(-22.756f, -1000f);

    @Test
    public void normaliseTest() {
        float length = (float) Math.sqrt(2);
        Assert.assertEquals(one.normalize(), new Vector2f(1 / length, 1 / length));
    }

    @Test
    public void negateTest() {
        Assert.assertEquals(one.negate(), new Vector2f(-1, -1));
        Assert.assertEquals(pos_neg.negate(), new Vector2f(-123, 425));
        Assert.assertEquals(neg_neg.negate(), new Vector2f(22.756f, 1000));
    }

    @Test
    public void lengthTest() {
        Assert.assertEquals(one.length(), (float) Math.sqrt(2.0));
        Assert.assertEquals(pos_neg.length(), (float) Math.sqrt(195754));
        Assert.assertEquals(neg_neg.length(), (float) Math.sqrt(1000517.835536f));
    }

    @Test
    public void lengthSquaredTest() {
        Assert.assertEquals(one.lengthSquared(), 2.0);
        Assert.assertEquals(pos_neg.lengthSquared(), 195754);
        Assert.assertEquals(neg_neg.lengthSquared(), 1000517.835536f);
    }

    @Test
    public void additionTest() {
        Vector2f other = new Vector2f(45, 77.89f);

        Vector2f simpleAdded = one.add(other);
        Assert.assertEquals(simpleAdded, new Vector2f(46, 78.89f));

        Vector2f pos_neg_Added = pos_neg.add(other);
        Assert.assertEquals(pos_neg_Added, new Vector2f(168, -347.11f));

        Vector2f neg_neg_Added = neg_neg.add(other);
        Assert.assertEquals(neg_neg_Added, new Vector2f(22.244f, -922.11f));
    }

    @Test
    public void subtractionTest() {
        Vector2f other = new Vector2f(45, 77.89f);

        Vector2f simpleAdded = one.subtract(other);
        Assert.assertEquals(simpleAdded, new Vector2f(-44, -76.89f));

        Vector2f pos_neg_Added = pos_neg.subtract(other);
        Assert.assertEquals(pos_neg_Added, new Vector2f(78, -502.89f));

        Vector2f neg_neg_Added = neg_neg.subtract(other);
        Assert.assertEquals(neg_neg_Added, new Vector2f(-67.756f, -1077.89f));
    }

    @Test
    public void scaleTest() {
        Vector2f simpleScaled = one.scale(45);
        Assert.assertEquals(simpleScaled, new Vector2f(45, 45));

        Vector2f pos_neg_scaled = pos_neg.scale(-15.75f);
        Assert.assertEquals(pos_neg_scaled, new Vector2f(-1937.25f, 6693.75f));

        Vector2f neg_neg_scaled = neg_neg.scale(42.73f);
        Assert.assertEquals(neg_neg_scaled, new Vector2f(-972.36388f, -42730));
    }

    @Test
    public void divideTest() {
        Assert.assertEquals(one.divide(2), new Vector2f(0.5f, 0.5f));
        Assert.assertEquals(pos_neg.divide(2), new Vector2f(61.5f, -212.5f));
        Assert.assertEquals(neg_neg.divide(0.00004f), new Vector2f(-568900, -25000000f));
        Assert.assertThrows(ArithmeticException.class, () -> one.divide(0));
    }

    @Test
    public void storeCoordinatesInBufferTest() {
        FloatBuffer manual;
        FloatBuffer test;
        try(MemoryStack stack = MemoryStack.stackPush()){
            manual = stack.mallocFloat(2);
            test = stack.mallocFloat(2);
            manual.put(1).put(1);
            manual.flip();
        }
        one.storeCoordinatesInBuffer(test);
        Assert.assertEquals(test, manual);
    }

    @Test
    public void getDistanceToOtherTest() {
        Assert.assertEquals(one.getDistanceToOther(one), 0);
        Assert.assertEquals(one.getDistanceToOther(new Vector2f()), (float) Math.sqrt(2));
    }

    @Test
    public void dotTest() {
        float dotProduct = pos_neg.getX() * neg_neg.getX() + pos_neg.getY() * neg_neg.getY();
        Assert.assertEquals(pos_neg.dot(neg_neg), dotProduct);
    }

    @Test
    public void lerpTest() {
        Vector2f two = new Vector2f(2, 2);
        Assert.assertEquals(new Vector2f(1.5f, 1.5f), two.lerp(one, 0.5f));
    }

}
