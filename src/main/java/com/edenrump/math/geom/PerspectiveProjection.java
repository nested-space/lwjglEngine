/*
 * Copyright (c) 2020 Ed Eden-Rump
 *
 * This file is part of Nested Engine.
 *
 * Nested Engine is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Nested Engine is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Nested Engine.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.edenrump.math.geom;

import com.edenrump.math.arrays.ColumnVector;
import com.edenrump.math.arrays.SquareMatrix;

public class PerspectiveProjection{

    private float width;
    private float height;

    private float horizontalBias;
    private float verticalBias;

    private float far;
    private float near;

    private SquareMatrix cacheProjectionMatrix;

    private PerspectiveProjection(float width, float height, float near, float far, float horizontalBias, float verticalBias){
        this.width = width;
        this.height = height;
        this.near = near;
        this.far = far;
        this.horizontalBias = horizontalBias;
        this.verticalBias = verticalBias;
        createProjectionMatrix();
    }

    public static PerspectiveProjection defineByOrthogonalPlanes(float left, float right, float bottom, float top, float near, float far){
        float width = right - left;
        float height = top - bottom;
        float horizontalBias = (right + left) / (right - left);
        float verticalBias = (top + bottom) / (top - bottom);
        return new PerspectiveProjection(width, height, near, far, horizontalBias, verticalBias);
    }

    public static PerspectiveProjection defineByFieldOfView(float fov, float aspect, float near, float far){
        float height = 2f * near * (float) Math.tan(Math.toRadians(fov / 2f));
        float width = aspect * height;
        return new PerspectiveProjection(width, height, near, far, 0, 0);
    }

    private void createProjectionMatrix() {
        float xScale = (2f * near) / width;
        float yScale = (2f * near) / height;
        float zScale = -(far + near) / (far - near);
        float zConstant = -(2f * far * near) / (far - near);
        ColumnVector c0 = new ColumnVector(xScale, 0, 0, 0);
        ColumnVector c1 = new ColumnVector(0, yScale, 0, 0);
        ColumnVector c2 = new ColumnVector(horizontalBias, verticalBias, zScale, -1f);
        ColumnVector c3 = new ColumnVector(0, 0, zConstant, 0);
        this.cacheProjectionMatrix = new SquareMatrix(c0, c1, c2, c3);
    }

    public SquareMatrix getProjectionMatrix() {
        return cacheProjectionMatrix;
    }

    public float getFOV(){
        return 2f * (float) Math.toDegrees(Math.atan(height / (2*near)));
    }

    public void setFOV(float fov){
        float aspect = width/height; //maintain aspect ratio
        float height = 2f * near * (float) Math.tan(Math.toRadians(fov / 2f));
        width = height * aspect;
        createProjectionMatrix();
    }

    public float getAspect(){
        return width / height;
    }

    public void setAspect(float aspect){
        this.width = this.height * aspect;
        createProjectionMatrix();
    }

    public float getHeight(){
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setHorizontalBias(float bias){
        this.horizontalBias = bias;
        createProjectionMatrix();
    }

    public void setVerticalBias(float bias){
        this.verticalBias = bias;
        createProjectionMatrix();
    }

    public void setNearPlaneDistance(float distance){
        this.near = distance;
        createProjectionMatrix();
    }

    public void setFarPlaneDistance(float distance){
        this.far = distance;
        createProjectionMatrix();
    }
}
