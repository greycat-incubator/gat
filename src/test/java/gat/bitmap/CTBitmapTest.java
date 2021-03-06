/**
 * Copyright 2017 the Greycat Additional Types authors.  All rights reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package gat.bitmap;

import gat.AdditionalTypesPlugin;
import gat.MockStorage;
import gat.bitset.CTBitset;
import greycat.Graph;
import greycat.GraphBuilder;
import greycat.Node;
import greycat.scheduler.NoopScheduler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CTBitmapTest {
    private Graph g;
    private Node n;

    @BeforeEach
    public void initGraph() {
        MockStorage storage = new MockStorage();
        g = GraphBuilder.newBuilder()
                .withPlugin(new AdditionalTypesPlugin())
                .withStorage(storage)
                .withScheduler(new NoopScheduler())
                .build();
        g.connect(null);
        n = g.newNode(0, 0);
    }

    @Test
    public void cardinality() {
        CTBitmap bitset = (CTBitmap) n.getOrCreateCustom("bitset", CTBitmap.NAME);
        bitset.set(0);
        bitset.set(10);
        bitset.set(17);
        bitset.set(2);
        bitset.set(4);

        assertEquals(5,bitset.cardinality());

    }

    @Test
    public void reload() {
        CTBitmap bitset = (CTBitmap) n.getOrCreateCustom("bitset", CTBitmap.NAME);
        bitset.set(0);
        bitset.set(10);
        bitset.set(17);
        bitset.set(2);
        bitset.set(4);

        bitset.save();
        CTBitmap bitset2 = (CTBitmap) n.getOrCreateCustom("bitset", CTBitmap.NAME);

        assertArrayEquals(bitset.getBitMap().toArray(),bitset2.getBitMap().toArray());
        assertEquals(bitset.cardinality(),bitset2.cardinality());
    }

    @Test
    public void next() {
        CTBitmap bitset = (CTBitmap) n.getOrCreateCustom("bitset", CTBitmap.NAME);
        bitset.set(0);
        bitset.set(10);
        bitset.set(17);
        bitset.set(2);
        bitset.set(4);
        assertEquals(0,bitset.nextSetBit(0));
        assertEquals(17,bitset.nextSetBit(11));
    }

    @AfterEach
    public void close() {
        n.free();
        g.disconnect(null);
    }
}