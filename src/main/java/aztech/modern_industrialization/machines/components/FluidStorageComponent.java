/*
 * MIT License
 *
 * Copyright (c) 2020 Azercoco & Technici4n
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package aztech.modern_industrialization.machines.components;

import aztech.modern_industrialization.machines.IComponent;
import com.google.common.base.Preconditions;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.minecraft.nbt.CompoundTag;

public class FluidStorageComponent implements IComponent {

    private long capacity;

    SingleVariantStorage<FluidVariant> singleStorageVariant = new SingleVariantStorage<FluidVariant>() {
        @Override
        protected FluidVariant getBlankVariant() {
            return FluidVariant.blank();
        }

        @Override
        protected long getCapacity(FluidVariant variant) {
            return capacity;
        }
    };

    public SingleVariantStorage<FluidVariant> getFluidStorage() {
        return singleStorageVariant;
    }

    public long getCapacity() {
        return capacity;
    }

    public void setCapacity(long capacity) {
        Preconditions.checkArgument(capacity >= 0, "Fluid Capacity must be > 0");
        this.capacity = capacity;
        singleStorageVariant.amount = Math.min(singleStorageVariant.amount, capacity);
    }

    public FluidVariant getFluid() {
        return singleStorageVariant.variant;
    }

    public long getAmount() {
        return singleStorageVariant.amount;
    }

    @Override
    public void writeNbt(CompoundTag tag) {
        tag.put("fluid", singleStorageVariant.variant.toNbt());
        tag.putLong("amount", singleStorageVariant.amount);
        tag.putLong("capacity", capacity);
    }

    @Override
    public void readNbt(CompoundTag tag) {
        singleStorageVariant.variant = FluidVariant.fromNbt(tag.getCompound("fluid"));
        singleStorageVariant.amount = tag.getLong("amount");
        capacity = tag.getLong("capacity");
    }
}
