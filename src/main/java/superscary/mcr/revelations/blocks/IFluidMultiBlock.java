package superscary.mcr.revelations.blocks;

public interface IFluidMultiBlock
{

    boolean isMaster ();
    boolean isSlave ();
    void initializeMasterIfNecessary();

}
