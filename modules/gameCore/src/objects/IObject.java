package objects;

import area.IArea;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

/**
 * @author Roman
 */
public interface IObject {

    int getSquareNumber();

    Color getColor();

    int getLastPosition();

    /**
     * @param SquareNumber Direction
     * @param area         Game area
     * @return Result
     */
    boolean move(int SquareNumber, IArea area);

    /**
     * The method can implement any action, works in conjunction with this.isActionable
     * Implementation in another classes
     *
     * @param SquareNumber Square number target
     * @param area         Game area
     * @return Result
     */
    boolean action(int SquareNumber, IArea area);

    /**
     * Method checks step valid for object
     *
     * @param SquareNumber Direction
     * @param area         Game area
     * @return Result
     */
    boolean isInRange(int SquareNumber, IArea area);

    /**
     * Method checks action valid for object
     *
     * @param SquareNumber Direction
     * @param area         Game area
     * @return Result
     */
    boolean isActionable(int SquareNumber, @NotNull IArea area);

    IObject clone() throws CloneNotSupportedException;
}