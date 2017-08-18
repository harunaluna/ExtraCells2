package extracells.integration.opencomputers;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import appeng.api.parts.IPart;
import appeng.api.parts.IPartHost;
import appeng.api.util.AEPartLocation;
import extracells.registries.ItemEnum;
import extracells.registries.PartEnum;
import li.cil.oc.api.driver.EnvironmentProvider;
import li.cil.oc.api.driver.SidedBlock;
import li.cil.oc.api.network.ManagedEnvironment;

public abstract class DriverBase<P extends IPart> implements SidedBlock, EnvironmentProvider {
	protected PartEnum part;
	protected Class environmentClass;

	public DriverBase(PartEnum part, Class environmentClass) {
		this.part = part;
		this.environmentClass = environmentClass;
	}

	//TODO: Use side ?
	@Override
	public boolean worksWith(World world, BlockPos pos, EnumFacing side) {
		P part = OCUtils.getPart(world, pos, AEPartLocation.INTERNAL);
		return part != null;
	}

	//TODO: Use side ?
	@Override
	public ManagedEnvironment createEnvironment(World world, BlockPos pos, EnumFacing side) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile == null || (!(tile instanceof IPartHost)))
			return null;
		return createEnvironment((IPartHost) tile);
	}

	protected abstract ManagedEnvironment createEnvironment(IPartHost host);

	@Override
	public Class<?> getEnvironment(ItemStack stack) {
		if(stack == null)
			return null;
		if(stack.getItem() == ItemEnum.PARTITEM.getItem() && stack.getItemDamage() == part.ordinal())
			return environmentClass;
		return null;
	}
}