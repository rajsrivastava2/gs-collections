/*
 * Copyright 2011 Goldman Sachs.
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

package com.gs.collections.impl.list.mutable;

import com.gs.collections.impl.test.Verify;
import org.junit.Test;

public class RandomAccessListAdapterSerializationTest
{
    @Test
    public void serializedForm()
    {
        Verify.assertSerializedForm(
                1L,
                "rO0ABXNyADxjb20uZ3MuY29sbGVjdGlvbnMuaW1wbC5saXN0Lm11dGFibGUuUmFuZG9tQWNjZXNz\n"
                        + "TGlzdEFkYXB0ZXIAAAAAAAAAAQIAAUwACGRlbGVnYXRldAAQTGphdmEvdXRpbC9MaXN0O3hwc3IA\n"
                        + "LWNvbS5ncy5jb2xsZWN0aW9ucy5pbXBsLmxpc3QubXV0YWJsZS5GYXN0TGlzdAAAAAAAAAABDAAA\n"
                        + "eHB3BAAAAAB4",
                new RandomAccessListAdapter<Object>(FastList.newList()));
    }
}
