/*
 * Copyright 2014 Goldman Sachs.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.gs.collections.impl.bag.mutable;

import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import com.gs.collections.api.bag.MutableBag;
import com.gs.collections.api.bag.ParallelUnsortedBag;
import com.gs.collections.api.block.procedure.Procedure;
import com.gs.collections.api.list.MutableList;
import com.gs.collections.api.set.ParallelUnsortedSetIterable;
import com.gs.collections.impl.block.factory.Functions;
import com.gs.collections.impl.block.factory.IntegerPredicates;
import com.gs.collections.impl.block.factory.Predicates;
import com.gs.collections.impl.block.procedure.CollectionAddProcedure;
import com.gs.collections.impl.factory.Lists;
import com.gs.collections.impl.list.mutable.FastList;
import com.gs.collections.impl.map.mutable.UnifiedMap;
import com.gs.collections.impl.test.Verify;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

public class HashBagTest extends MutableBagTestCase
{
    @Override
    protected <T> HashBag<T> newWith(T... littleElements)
    {
        return HashBag.newBagWith(littleElements);
    }

    @Test
    public void newBagWith()
    {
        HashBag<String> bag = new HashBag<String>().with("apple", "apple");
        assertBagsEqual(HashBag.newBagWith("apple", "apple"), bag);

        bag.with("hope", "hope", "hope");
        assertBagsEqual(HashBag.newBagWith("apple", "apple", "hope", "hope", "hope"), bag);

        bag.withAll(Collections.nCopies(5, "ubermench"));
        Assert.assertEquals(
                UnifiedMap.newWithKeysValues(
                        "apple", 2,
                        "hope", 3,
                        "ubermench", 5),
                bag.toMapOfItemToCount());
    }

    @Test
    public void newBagFromIterable()
    {
        assertBagsEqual(
                HashBag.newBagWith(1, 2, 2, 3, 3, 3),
                HashBag.newBag(FastList.newListWith(1, 2, 2, 3, 3, 3)));
    }

    @Test
    public void newBagFromBag()
    {
        Assert.assertEquals(
                HashBag.newBagWith(1, 2, 2, 3, 3, 3, 4, 4, 4, 4),
                HashBag.newBag(HashBag.newBagWith(1, 2, 2, 3, 3, 3, 4, 4, 4, 4)));
    }

    @Ignore
    @Test
    public void asParallel()
    {
        MutableBag<String> result = HashBag.<String>newBag().asSynchronized();
        HashBag.newBagWith(1, 1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .asParallel(Executors.newFixedThreadPool(10), 1)
                .select(IntegerPredicates.isOdd())
                .collect(Functions.getToString())
                .forEach(CollectionAddProcedure.on(result));
        Assert.assertEquals(
                HashBag.newBagWith("1", "1", "1", "3", "5", "7", "9"),
                result);
    }

    @Ignore
    @Test
    public void asParallel_allSatisfy()
    {
        Assert.assertTrue(HashBag.newBagWith(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .asParallel(Executors.newFixedThreadPool(10), 2)
                .select(IntegerPredicates.isOdd())
                .collect(Functions.getToString())
                .allSatisfy(Predicates.in(Lists.mutable.of("1", "3", "5", "7", "9"))));

        Assert.assertFalse(HashBag.newBagWith(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .asParallel(Executors.newFixedThreadPool(10), 2)
                .select(IntegerPredicates.isOdd())
                .collect(Functions.getToString())
                .allSatisfy(Predicates.in(Lists.mutable.of("1", "3", "7"))));

        Assert.assertTrue(HashBag.newBagWith(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .asParallel(Executors.newFixedThreadPool(10), 2)
                .select(IntegerPredicates.isOdd())
                .collect(Functions.<Integer>getPassThru())
                .allSatisfy(IntegerPredicates.isPositive()));

        Assert.assertFalse(HashBag.newBagWith(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .asParallel(Executors.newFixedThreadPool(10), 2)
                .select(IntegerPredicates.isOdd())
                .allSatisfy(Predicates.lessThan(7)));

        Assert.assertTrue(HashBag.<Integer>newBag()
                .asParallel(Executors.newFixedThreadPool(10), 2)
                .select(IntegerPredicates.isOdd())
                .allSatisfy(Predicates.greaterThan(10)));

        Assert.assertFalse(HashBag.newBagWith(1)
                .asParallel(Executors.newFixedThreadPool(10), 2)
                .select(IntegerPredicates.isOdd())
                .allSatisfy(Predicates.greaterThan(10)));

        Assert.assertTrue(HashBag.newBagWith(1)
                .asParallel(Executors.newFixedThreadPool(10), 2)
                .select(IntegerPredicates.isEven())
                .allSatisfy(Predicates.greaterThan(10)));
    }

    @Ignore
    @Test
    public void asParallel2()
    {
        MutableBag<String> result = HashBag.newBagWith(1, 1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .asParallel(Executors.newFixedThreadPool(10), 1)
                .select(IntegerPredicates.isOdd())
                .collect(Functions.getToString())
                .toBag();
        Assert.assertEquals(
                HashBag.newBagWith("1", "1", "1", "3", "5", "7", "9"),
                result);
    }

    @Ignore
    @Test
    public void asParallel_asUnique()
    {
        ParallelUnsortedBag<Integer> integers = this.newWith(1, 2, 2, 3, 3, 3, 4, 4, 4, 4).asParallel(Executors.newFixedThreadPool(10), 2);
        ParallelUnsortedSetIterable<Integer> unique = integers.asUnique();
        Assert.assertNotSame(integers, unique);
        final AtomicInteger atomicInteger = new AtomicInteger();
        unique.forEach(new Procedure<Integer>()
        {
            public void value(Integer each)
            {
                atomicInteger.incrementAndGet();
            }
        });
        Assert.assertEquals(4, atomicInteger.get());
    }

    @Ignore
    @Test
    public void asParallel_select()
    {
        MutableList<Integer> result = this.newWith(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .asParallel(Executors.newFixedThreadPool(10), 2)
                .select(IntegerPredicates.isOdd())
                .toList();
        Verify.assertContainsAll(result, 1, 3, 5, 7, 9);
        Verify.assertSize(5, result);
    }

    @Ignore
    @Test
    public void asParallel_toList()
    {
        ExecutorService service = Executors.newFixedThreadPool(10);
        MutableList<Integer> actual = this.newWith(1, 1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .asParallel(service, 2)
                .toList();
        Verify.assertContainsAll(actual, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        Verify.assertSize(12, actual);
    }
}
