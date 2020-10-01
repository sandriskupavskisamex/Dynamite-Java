package training.dynamite;

import com.softwire.dynamite.bot.Bot;
import com.softwire.dynamite.game.*;

import java.util.Random;


public class MyBot implements Bot {

    public MyBot() {
        // Are you debugging?
        // Put a breakpoint on the line below to see when we start a new match
        System.out.println("Started new match");
    }

    int dynamitePlayed = 0;


    @Override
    public Move makeMove(Gamestate gamestate) {
        // Are you debugging?
        // Put a breakpoint in this method to see when we make a move
        Move myMove = Move.R;


        if (checkIfLast5MovesAreSame(gamestate)) {
            myMove = beatLastFiveSameMoves(gamestate);
        } else if (areTheyBeatingMyPreviousMove(gamestate) != null) {
            myMove = areTheyBeatingMyPreviousMove(gamestate);
        } else if (checkIfOpponentPossiblyRandom(gamestate)) {
            myMove = beatRandomOpponent(gamestate);
        } else {
            myMove = getRandomWithoutWater();
        }

        if (myMove == Move.D) {
            dynamitePlayed++;
        }


        return myMove;

    }

    private Move getRandomWithoutWater() {
        Random rand = new Random();

        Move myMove = Move.R;

        int x = rand.nextInt((4 - 1) + 1) + 1;

        if (dynamitePlayed < 100) {

            switch (x) {
                case 1:
                    myMove = Move.R;
                    break;
                case 2:
                    myMove = Move.P;
                    break;
                case 3:
                    myMove = Move.S;
                    break;
                case 4:
                    myMove = Move.D;

            }

        } else {
            x = rand.nextInt((3 - 1) + 1) + 1;
            switch (x) {
                case 1:
                    myMove = Move.R;
                    break;
                case 2:
                    myMove = Move.P;
                    break;
                case 3:
                    myMove = Move.S;
                    break;
            }
        }
        return myMove;
    }

    private boolean checkIfLast5MovesAreSame(Gamestate gamestate) {

        int lastMovesSameCount = 0;

        boolean checker = false;

        if (gamestate.getRounds().size() > 5) {

            for (int i = gamestate.getRounds().size() - 6; i < gamestate.getRounds().size() - 1; i++) {
                if (gamestate.getRounds().get(i).getP2() == gamestate.getRounds().get(i + 1).getP2()) {
                    lastMovesSameCount = lastMovesSameCount + 1;
                }
            }

            if (lastMovesSameCount >= 5) {
                checker = true;
            } else {
                checker = false;
            }
        }
        return checker;
    }

    private Move beatLastFiveSameMoves(Gamestate gamestate) {
        Move myMove = Move.R;

        Move lastMove = gamestate.getRounds().get(gamestate.getRounds().size() - 1).getP2();

        switch (lastMove) {
            case R:
                myMove = Move.P;
                break;
            case P:
                myMove = Move.S;
                break;
            case S:
                myMove = Move.R;
                break;
            case D:
                myMove = Move.W;
                break;
            case W:
                myMove = Move.W;
        }

        return myMove;
    }

    private boolean checkIfOpponentPossiblyRandom(Gamestate gamestate) {

        int lastMovesDiffCount = 0;

        if (gamestate.getRounds().size() > 4) {

            for (int i = gamestate.getRounds().size() - 4; i < gamestate.getRounds().size() - 1; i++) {

                if (gamestate.getRounds().get(i).getP2() != gamestate.getRounds().get(i + 1).getP2()) {
                    lastMovesDiffCount = lastMovesDiffCount + 1;
                }
            }

            if (lastMovesDiffCount >= 3) {
                return true;
            }

        }

        return false;
    }

    private Move beatRandomOpponent(Gamestate gamestate) {

        Move myMove = gamestate.getRounds().get(gamestate.getRounds().size() - 1).getP2();

        if(myMove == Move.D && dynamitePlayed >= 100){
            myMove = getRandomWithoutWater();
        }

        return myMove;
    }

    private Move areTheyBeatingMyPreviousMove(Gamestate gamestate) {
        Move myMove = null;
        int beatingPrevMoveCount = 0;

        if (gamestate.getRounds().size() > 5) {

            for (int i = gamestate.getRounds().size() - 5; i < gamestate.getRounds().size(); i++) {
                if (xWinsAgainstY(gamestate.getRounds().get(i).getP2(), gamestate.getRounds().get(i - 1).getP1())) {
                    beatingPrevMoveCount++;
                }
            }

            Move lastMove = gamestate.getRounds().get(gamestate.getRounds().size() - 1).getP1();

            if (beatingPrevMoveCount >= 5) {
                switch (lastMove) {
                    case R:
                        myMove = Move.S;
                        break;
                    case P:
                        myMove = Move.R;
                        break;
                    case S:
                        myMove = Move.P;
                        break;
                    case D:
                        myMove = Move.R;
                        break;
                    case W:
                        myMove = getRandomWithoutWater();
                }
            }

        }

        return myMove;
    }

    private static boolean xWinsAgainstY(Move x, Move y) {
        if (x != Move.R || y != Move.S && y != Move.W) {
            if (x != Move.P || y != Move.R && y != Move.W) {
                if (x == Move.S && (y == Move.P || y == Move.W)) {
                    return true;
                } else if (x != Move.D || y != Move.R && y != Move.P && y != Move.S) {
                    return x == Move.W && y == Move.D;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

}
