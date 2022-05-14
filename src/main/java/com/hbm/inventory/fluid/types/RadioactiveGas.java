package com.hbm.inventory.fluid.types;

import com.hbm.inventory.fluid.FluidType;
import com.hbm.render.util.EnumSymbol;

public class RadioactiveGas extends FluidType {

	public RadioactiveGas(String name, int color, int p, int f, int r, EnumSymbol symbol) {
		super(name, color, p, f, r, symbol);
		this.addTraits(FluidTrait.GASEOUS);
	}
}