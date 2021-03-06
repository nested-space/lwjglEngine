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

package com.edenrump.math.shape.mesh;

public class Face {

    private final Vertex v1;
    private final Vertex v2;
    private final Vertex v3;

    public Face(Vertex v1, Vertex v2, Vertex v3) {
        if (v1 == null || v2 == null || v3 == null)
            throw new IllegalArgumentException("Cannot create face with null vertices");

        if (v1 == v2 || v2 == v3 || v3 == v1)
            throw new IllegalArgumentException("Faces cannot contain identical vertices");

        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }

    public Vertex getV1() {
        return v1;
    }

    public Vertex getV2() {
        return v2;
    }

    public Vertex getV3() {
        return v3;
    }

    @Override
    public int hashCode() {
        return v1.hashCode() * 17 +
                v2.hashCode() * 23 +
                v3.hashCode() * 27;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Face) {
            Face face = (Face) obj;
            return this.v1 == face.v1 && this.v2 == face.v2 && this.v3 == face.v3;
        }
        return super.equals(obj);
    }
}
