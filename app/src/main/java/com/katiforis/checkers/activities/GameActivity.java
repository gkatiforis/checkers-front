package com.katiforis.checkers.activities;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.katiforis.checkers.DTO.LevelEnum;
import com.katiforis.checkers.DTO.UserDto;
import com.katiforis.checkers.DTO.response.GameState;
import com.katiforis.checkers.DTO.PlayerAnswer;
import com.katiforis.checkers.DTO.response.OfferDraw;
import com.katiforis.checkers.R;
import com.katiforis.checkers.conf.Const;
import com.katiforis.checkers.controller.GameController;
import com.katiforis.checkers.controller.HomeController;
import com.katiforis.checkers.game.Board;
import com.katiforis.checkers.game.Cell;
import com.katiforis.checkers.game.Move;
import com.katiforis.checkers.game.Piece;
import com.katiforis.checkers.observer.ConnectionObserver;
import com.katiforis.checkers.stomp.Client;
import com.katiforis.checkers.util.AudioPlayer;
import com.katiforis.checkers.util.CircleTransform;
import com.katiforis.checkers.util.LocalCache;
import com.katiforis.checkers.util.Utils;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import info.hoang8f.widget.FButton;
import me.itangqi.waveloadingview.WaveLoadingView;
import tyrantgit.explosionfield.ExplosionField;

import static com.katiforis.checkers.DTO.LevelEnum.LEVEL_1;
import static com.katiforis.checkers.DTO.LevelEnum.LEVEL_2;
import static com.katiforis.checkers.DTO.LevelEnum.LEVEL_3;
import static com.katiforis.checkers.DTO.LevelEnum.LEVEL_4;
import static com.katiforis.checkers.DTO.LevelEnum.LEVEL_5;
import static com.katiforis.checkers.DTO.LevelEnum.LEVEL_6;
import static com.katiforis.checkers.DTO.LevelEnum.mapPointsToLevel;
import static com.katiforis.checkers.conf.Const.OFFER_DRAW_TIME_IN_SECONDS;
import static com.katiforis.checkers.util.CachedObjectProperties.CURRENT_GAME_ID;
import static com.katiforis.checkers.util.CachedObjectProperties.USER_ID;
import static com.katiforis.checkers.util.Utils.getDiffInSeconds;
import static com.katiforis.checkers.util.Utils.twoDigits;

public class GameActivity extends AppCompatActivity implements ConnectionObserver {
    private static final int DARK_PIECE_ICON = R.drawable.redpf;
    private static final int DARK_KING_PIECE_ICON = R.drawable.redpk;
    private static final int DARK_PIECE_PRESSED = R.drawable.redpf;
    private static final int DARK_KING_PIECE_PRESSED = R.drawable.redpk;
    private static final int DARK_PIECE_HIGHLIGHTED = R.drawable.redpf;
    private static final int DARK_KING_PIECE_HIGHLIGHTED = R.drawable.redpk;

    private static final int LIGHT_PIECE_ICON = R.drawable.whitept;
    private static final int LIGHT_KING_PIECE_ICON = R.drawable.whitepk;
    private static final int LIGHT_PIECE_PRESSED = R.drawable.whitept;
    private static final int LIGHT_KING_PIECE_PRESSED = R.drawable.whitepk;
    private static final int LIGHT_PIECE_HIGHLIGHTED = R.drawable.whitept;
    private static final int LIGHT_KING_PIECE_HIGHLIGHTED = R.drawable.whitepk;

    private static final int POSSIBLE_MOVE = R.drawable.circle_move2;
    private static final String LAST_MOVE_COLOR = "#55000000";

    private ImageView gameOptionsDialogButton;
    private ImageView playerImage;
    private TextView username;
    private TextView elo;
    private ImageView eloImage;
    private TextView lvl;
    private WaveLoadingView player1Time;

    private ImageView playerImage2;
    private TextView username2;
    private TextView elo2;
    private ImageView eloImage2;
    private TextView lvl2;
    private WaveLoadingView player2Time;

    private CountDownTimer countDownTimer;
    private long dateStarted;
    private long dateCurrent;
    private ExplosionField explosionField;

    private Snackbar snack;


    private GameController gameController;
    private HomeController homeController;

    public static boolean populated;

    private Integer gameMaxTime;
    private int[] buttons_id;
    private ImageButton[][] buttonBoard;
    private List<Cell> highlightedCells;
    private List<Move> moves;
    private UserDto player1, player2, currentPlayer;
    private boolean srcCellFixed;
    private Board cellBoard = new Board();
    private Cell srcCell, dstCell;

    private FButton resignButton;
    private FButton offerDrawButton;
    private FButton mainMenuButton;
    private ImageView cancel;

    private DialogPlus optionsDialog;
    private ViewGroup optionsDialogView;

    private DialogPlus noInternetDialog;
    private ViewGroup noInternetDialogView;

    private Animation anim;
    private AdView mAdView;

    private AudioPlayer audioPlayer;

    public boolean isConnected = true;


    private Cell lastSelected;
    private int lastMotionEvent;
    boolean dragStarted = false;
    boolean toEmptyCell = false;
    int moveCounts = 0;


    private void intentToMenuActivity() {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setClass(this, MenuActivity.class);
        startActivity(intent);
    }


    public void setGameState(GameState gamestate) {
        runOnUiThread(() -> {
            Log.i(Const.TAG, "GameState：" + gamestate);
            initComponents();

            if (gamestate.getGameStatus() == GameState.Status.PLAYERS_SELECTION) {
                gameController.openGameStatsDialog(gamestate.getGameStats());
            } else if (gamestate.getGameStatus() == GameState.Status.TERMINATED) {
                intentToMenuActivity();
            } else {
                gameController.closeGameStatsDialog();
                setPlayerList(gamestate.getPlayers());
                dateStarted = gamestate.getDateStarted().getTime();
                dateCurrent = gamestate.getCurrentDate().getTime();
                gameMaxTime = gamestate.getGameMaxTime();

                final Handler someHandler = new Handler(getMainLooper());

                someHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Date date = new Date(dateCurrent);
                        date.setTime(date.getTime() + 1000);
                        dateCurrent = date.getTime();

                        someHandler.postDelayed(this, 1000);
                    }
                }, 10);

                this.player1 = gamestate.getWhitePlayer();
                this.player2 = gamestate.getDarkPlayer();
                this.currentPlayer = gamestate.getCurrentPlayer();


                long remainingTime = currentPlayer.getSecondsRemaining() -
                        Utils.getDiffInSeconds(gamestate.getLastMoveDate(), gamestate.getCurrentDate());
                currentPlayer.setSecondsRemaining(remainingTime);

                setPlayerTime(gamestate.getPrevPlayer(), this.currentPlayer);

                updateTurnTracker();

                setBoard(gamestate.getBoard());

                if (this.player1.getUserId().equals(LocalCache.getInstance().getString(USER_ID, this)) &&
                        player1.getColor().equals(Piece.LIGHT)) {
                    FrameLayout mView = findViewById(R.id.gameboard);
                    mView.setRotation(180);
                    ViewGroup.LayoutParams lp = mView.getLayoutParams();
                } else if (this.player2.getUserId().equals(LocalCache.getInstance().getString(USER_ID, this)) &&
                        player2.getColor().equals(Piece.LIGHT)) {
                    FrameLayout mView = findViewById(R.id.gameboard);
                    mView.setRotation(180);
                    ViewGroup.LayoutParams lp = mView.getLayoutParams();
                }


                if (gamestate.getOfferDrawUserId() != null &&
                        !gamestate.getOfferDrawUserId().equals(LocalCache.getInstance().getString(USER_ID, this)) &&
                        getDiffInSeconds(gamestate.getOfferDrawDate(), gamestate.getCurrentDate()) <= OFFER_DRAW_TIME_IN_SECONDS) {
                    this.runOnUiThread(() -> {
                        OfferDraw offerDraw = new OfferDraw();
                        offerDraw.setByUser(gamestate.getOfferDrawUserId());
                        showOfferDraw(offerDraw);
                    });
                }
            }
        });
    }

    void initComponents() {
        getSupportActionBar().hide();
        explosionField = ExplosionField.attach2Window(this);
        View user1 = findViewById(R.id.user1);
        playerImage = user1.findViewById(R.id.playerImage);
        username = user1.findViewById(R.id.username);
        elo = user1.findViewById(R.id.elo);
        eloImage = user1.findViewById(R.id.eloImage);
        lvl = user1.findViewById(R.id.lvl);
        gameOptionsDialogButton = findViewById(R.id.gameOptionsDialogButton);

        player1Time = (WaveLoadingView) user1.findViewById(R.id.playerTime);
        player1Time.setShapeType(WaveLoadingView.ShapeType.CIRCLE);
        player1Time.setProgressValue(100);
        player1Time.setBorderWidth(10);
        player1Time.setAmplitudeRatio(60);
        player1Time.setCenterTitleColor(Color.WHITE);
        player1Time.setAnimDuration(1000);
        player1Time.pauseAnimation();
        player1Time.resumeAnimation();
        player1Time.cancelAnimation();
        player1Time.startAnimation();


        View user2 = findViewById(R.id.user2);
        playerImage2 = user2.findViewById(R.id.playerImage);
        elo2 = user2.findViewById(R.id.elo);
        eloImage2 = user2.findViewById(R.id.eloImage);
        lvl2 = user2.findViewById(R.id.lvl);
        username2 = user2.findViewById(R.id.username);

        player2Time = (WaveLoadingView) user2.findViewById(R.id.playerTime);
        player2Time.setShapeType(WaveLoadingView.ShapeType.CIRCLE);
        player2Time.setProgressValue(100);
        player2Time.setBorderWidth(10);
        player2Time.setAmplitudeRatio(60);
        player2Time.setCenterTitleColor(Color.WHITE);
        player2Time.setAnimDuration(1000);
        player2Time.pauseAnimation();
        player2Time.resumeAnimation();
        player2Time.cancelAnimation();
        player2Time.startAnimation();


        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            this.resizeBoardToScreenSizePortrait();
        }
//		else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
//			//this.resizeBoardToScreenSizeLandscape();
//		}
        srcCell = null;
        dstCell = null;
        srcCellFixed = false;
        highlightedCells = new ArrayList<>();
        buttons_id = getButtonArray();
        buttonBoard = new ImageButton[8][8];
        fillButtonBoard();
        this.moves = new ArrayList<>();
    }

    public void showOfferDraw(OfferDraw offerDraw) {
        runOnUiThread(() -> {

            if (!offerDraw.getByUser().equals(LocalCache.getInstance().getString(USER_ID, this))) {
                if (optionsDialog == null || !optionsDialog.isShowing()) {
                    showGameOptionsDialog(true);
                }

                anim = new AlphaAnimation(0.4f, 1.0f);
                anim.setDuration(500);
                anim.setStartOffset(20);
                anim.setRepeatMode(Animation.REVERSE);
                anim.setRepeatCount(30);
                offerDrawButton.startAnimation(anim);

                offerDrawButton.setText("Your opponent has proposed draw");
                final Handler handler = new Handler(getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        offerDrawButton.setText("Offer Draw");
                    }
                }, 30 * 500);
            }
        });
    }

    public void sendResign(boolean goMainMenu) {
        PlayerAnswer playerAnswer = new PlayerAnswer();
        playerAnswer.setResign(true);
        gameController.sendAnswer(LocalCache.getInstance().getString(CURRENT_GAME_ID, this), playerAnswer)
                .subscribe(
                        () -> {
                            if (goMainMenu) {
                                intentToMenuActivity();
                            } else {
                                audioPlayer.playPopup();
                                showGameOptionsDialog(false);
                            }
                        },
                        throwable -> {
                        });
    }

    public void sendOfferDraw() {
        PlayerAnswer playerAnswer = new PlayerAnswer();
        playerAnswer.setOfferDraw(true);
        gameController.sendAnswer(LocalCache.getInstance().getString(CURRENT_GAME_ID, this), playerAnswer)
                .subscribe(
                        () -> {
                            audioPlayer.playPopup();
                            showGameOptionsDialog(false);
                        },
                        throwable -> {
                        });
    }

    private void updatePlayerScore(String username, String points) {
        //TODO
//		for (UserDto player :userList){
//			if(player.getUsername().equals(username)){
//				player.getPlayerDetails().setElo(player.getPlayerDetails().getElo() + Integer.valueOf(points));
//			}
//		}
//		playerAdapter.notifyDataSetChanged();
    }

    void setPlayerList(List<UserDto> players) {

        UserDto player = players.get(0);
        UserDto player2 = players.get(1);

        if (player2.getUserId().equals(LocalCache.getInstance().getString(USER_ID, this))) {

            Picasso.with(this)
                    .load(player2.getPictureUrl())
                    .placeholder(getResources().getDrawable(R.drawable.user))
                    .error(R.mipmap.ic_launcher)
                    .transform(new CircleTransform())
                    .into(playerImage2, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError() {
                            //TODO
                        }
                    });
            Picasso.with(this)
                    .load(player.getPictureUrl())
                    .placeholder(getResources().getDrawable(R.drawable.user))
                    .error(R.mipmap.ic_launcher)
                    .transform(new CircleTransform())
                    .into(playerImage, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError() {
                            //TODO
                        }
                    });
            username.setText(player.getUsername());
            elo.setText(String.valueOf(player.getPlayerDetails().getElo()));
            setEloImage(player.getPlayerDetails().getElo(), eloImage);
            lvl.setText(String.valueOf(player.getPlayerDetails().getLevel()));

            username2.setText(player2.getUsername());
            elo2.setText(String.valueOf(player2.getPlayerDetails().getElo()));
            setEloImage(player2.getPlayerDetails().getElo(), eloImage2);
            lvl2.setText(String.valueOf(player2.getPlayerDetails().getLevel()));
        } else {
            Picasso.with(this)
                    .load(player2.getPictureUrl())
                    .placeholder(getResources().getDrawable(R.drawable.user))
                    .error(R.mipmap.ic_launcher)
                    .transform(new CircleTransform())
                    .into(playerImage, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError() {
                            //TODO
                        }
                    });
            Picasso.with(this)
                    .load(player.getPictureUrl())
                    .placeholder(getResources().getDrawable(R.drawable.user))
                    .error(R.mipmap.ic_launcher)
                    .transform(new CircleTransform())
                    .into(playerImage2, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError() {
                            //TODO
                        }
                    });
            username.setText(player2.getUsername());
            elo.setText(String.valueOf(player2.getPlayerDetails().getElo()));
            setEloImage(player2.getPlayerDetails().getElo(), eloImage);
            lvl.setText(String.valueOf(player2.getPlayerDetails().getLevel()));

            username2.setText(player.getUsername());
            elo2.setText(String.valueOf(player.getPlayerDetails().getElo()));
            setEloImage(player.getPlayerDetails().getElo(), eloImage2);
            lvl2.setText(String.valueOf(player.getPlayerDetails().getLevel()));
        }
    }

    public void setEloImage(int elo, ImageView pointsImage) {
        LevelEnum level = mapPointsToLevel(elo);
        if (level == LEVEL_1)
            pointsImage.setImageResource(R.drawable.level1);
        else if (level == LEVEL_2)
            pointsImage.setImageResource(R.drawable.level2);
        else if (level == LEVEL_3)
            pointsImage.setImageResource(R.drawable.level3);
        else if (level == LEVEL_4)
            pointsImage.setImageResource(R.drawable.level4);
        else if (level == LEVEL_5)
            pointsImage.setImageResource(R.drawable.level5);
        else if (level == LEVEL_6)
            pointsImage.setImageResource(R.drawable.level6);
    }

    public void sendMove(Move move) {
        PlayerAnswer playerAnswer = new PlayerAnswer();
        playerAnswer.setMove(move);
        gameController.sendAnswer(LocalCache.getInstance().getString(CURRENT_GAME_ID, this), playerAnswer)
                .subscribe(() -> {
                        },
                        throwable -> {
                        });
    }


    public void makeMove(PlayerAnswer playerAnswer) {
        runOnUiThread(() -> {
            UserDto current, prev;
            if (playerAnswer.getPlayers().get(0).getCurrent()) {
                current = playerAnswer.getPlayers().get(0);
                prev = playerAnswer.getPlayers().get(1);
            } else {
                lastSelected = null;
                current = playerAnswer.getPlayers().get(1);
                prev = playerAnswer.getPlayers().get(0);
            }
            makeMove(prev, current, playerAnswer.getMove().getFrom(), playerAnswer.getMove().getTo());
        });
    }

    private void setBoard(Board board) {
        this.cellBoard = board;
        updateBoard(buttonBoard, cellBoard);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_layout);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        audioPlayer = new AudioPlayer(this);
        Client.getInstance().registerObserver(this);

        initComponents();
        populated = false;

        gameController = GameController.getInstance();
        gameController.setGameActivity(this);

        homeController = HomeController.getInstance();
        homeController.setGameActivity(this);

        optionsDialogView = (ViewGroup) getLayoutInflater().inflate(R.layout.fragment_game_options_layout, null);
        offerDrawButton = optionsDialogView.findViewById(R.id.offerDraw);
        resignButton = optionsDialogView.findViewById(R.id.resign);
        mainMenuButton = optionsDialogView.findViewById(R.id.mainMenu);
        cancel = optionsDialogView.findViewById(R.id.cancel);
        resignButton.setButtonColor(getResources().getColor(R.color.fbutton_color_pomegranate));
        offerDrawButton.setButtonColor(getResources().getColor(R.color.fbutton_color_clouds));
        mainMenuButton.setButtonColor(getResources().getColor(R.color.fbutton_color_clouds));


        gameOptionsDialogButton.setOnClickListener(p -> {
            showGameOptionsDialog(true);
        });


        noInternetDialogView = (ViewGroup) getLayoutInflater().inflate(R.layout.fragment_connection_lost_layout, null);

        noInternetDialog = DialogPlus.newDialog(this)
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                        System.out.println(item);
                    }
                }).setContentHolder(new ViewHolder(noInternetDialogView))
                .setGravity(Gravity.TOP)
                .setCancelable(false)
                .create();
    }


    public void showGameOptionsDialog(boolean show) {

        if (show) {
            optionsDialog = DialogPlus.newDialog(this)
                    .setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                            System.out.println(item);
                        }
                    }).setContentHolder(new ViewHolder(optionsDialogView))
                    .setCancelable(true)
                    .create();

            offerDrawButton.setOnClickListener(pa -> {
                audioPlayer.playClickButton();
                offerDrawButton.setText("Offer Draw");

                if (anim != null) {
                    anim.cancel();
                }

                sendOfferDraw();
            });

            resignButton.setOnClickListener(pa -> {
                audioPlayer.playClickButton();
                sendResign(false);
            });

            mainMenuButton.setOnClickListener(pa -> {
                audioPlayer.playClickButton();
                sendResign(true);
            });

            cancel.setOnClickListener(pa -> {
                audioPlayer.playPopup();
                optionsDialog.dismiss();
            });

            audioPlayer.playPopup();
            optionsDialog.show();
        } else {
            optionsDialog.dismiss();
            optionsDialog = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();

        String gameId = LocalCache.getInstance().getString(CURRENT_GAME_ID, this);
        if (gameId == null || gameId.length() == 0) {
            return;
        }
        gameController.getGameState();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        audioPlayer.release();
    }

    private View.OnTouchListener listener2 = (View view, MotionEvent motionEvent) -> {

        int tag = (Integer) view.getTag();
        int xCord = tag / 10;
        int yCord = tag % 10;
        Cell currentCell = cellBoard.getCell(xCord, yCord);

        String userId = LocalCache.getInstance().getString(USER_ID, this);
        if (!currentPlayer.getUserId().equals(userId) ||
                (currentCell.getPlacedPiece() != null &&
                        !currentPlayer.getColor().equals(currentCell.getPlacedPiece().getColor()))) {
            return false;
        }

        if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            lastMotionEvent = MotionEvent.ACTION_DOWN;
            moveCounts = 0;

            if (currentCell.getPlacedPiece() == null) {
                toEmptyCell = true;
            } else {
                toEmptyCell = false;
            }
            if (lastSelected != null && lastSelected.equals(currentCell)) {
                srcCell = currentCell;
                setvisible();
                return true;
            } else if (lastSelected != null && !lastSelected.equals(currentCell)) {
                if (currentCell.containsPiece()) {
                    srcCell = null;
                }
                clearPossibleMoves();
            }
            lastSelected = currentCell;
            playerTurn(xCord, yCord);
            return true;
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
            view.setVisibility(View.VISIBLE);
            lastMotionEvent = MotionEvent.ACTION_UP;
            setvisible();
            return true;
        } else if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
            if (toEmptyCell) {
                return true;
            }
            moveCounts++;

            if (moveCounts < 5) {
                return true;
            }
            if (lastMotionEvent != MotionEvent.ACTION_MOVE) {
                view.setVisibility(View.INVISIBLE);
                lastMotionEvent = MotionEvent.ACTION_MOVE;
                return true;
            } else {
                lastMotionEvent = MotionEvent.ACTION_MOVE;
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                        view);
                shadowBuilder.getView().setAlpha(1);

                view.startDrag(data, shadowBuilder, view, 0);
                return true;
            }

        } else {
            return true;
        }
    };

    private View.OnDragListener listener3 = (View v, DragEvent event) -> {

        if (!currentPlayer.getUserId().equals(LocalCache.getInstance().getString(USER_ID, this))) {
            return false;
        }

        int tag = (Integer) v.getTag();
        int xCord = tag / 10;
        int yCord = tag % 10;

        View view = (View) event.getLocalState();
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                dragStarted = true;
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                break;
            case DragEvent.ACTION_DROP:
                if (view instanceof ImageButton) {
                    if (srcCell == null ||
                            (!cellBoard.isPossibleMove(srcCell.getX(), srcCell.getY(), xCord, yCord) &&
                                    !srcCellFixed) || (srcCell.getX() == xCord && srcCell.getY() == yCord)) {
                        view.setVisibility(View.VISIBLE);
                    } else {

                    }
                    playerTurn(xCord, yCord);
                } else {
                    view.setVisibility(View.VISIBLE);
                }
                setvisible();
                break;
            case DragEvent.ACTION_DRAG_ENDED:
                moveCounts = 0;
                final View droppedView = (View) event.getLocalState();
                droppedView.post(new Runnable() {
                    @Override
                    public void run() {
                        dragStarted = false;
                        setvisible();
                        view.setVisibility(View.VISIBLE);
                        if (view instanceof ImageButton) {
                            srcCell = lastSelected;
                        }
                    }
                });
                break;
            default:
                break;
        }

        return true;
    };


    public void setvisible() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 == 0) {
                    buttonBoard[i][j].setVisibility(View.VISIBLE);
                }
            }
        }
    }

    public void playerTurn(int xCord, int yCord) {

        if (cellBoard.hasMoves(Piece.DARK) && cellBoard.hasMoves(Piece.LIGHT)) {

            if (cellBoard.getCell(xCord, yCord).containsPiece() && cellBoard.getCell(xCord, yCord).getPlacedPiece().getColor().equals(currentPlayer.getColor()) && srcCell == null) {
                unHighlightPieces();
                srcCell = cellBoard.getCell(xCord, yCord);

                List<Cell> currentPlayerPieces = cellBoard.getCellPieces(this.currentPlayer.getColor());
                moves = cellBoard.possibleMoves(currentPlayerPieces);


                if (!moveExistFrom(srcCell)) {
                    srcCell = null;
                    updatePieces(xCord, yCord);
                    updateTurnTracker();
                    return;
                }
                if (moves.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "No possible moves!", Toast.LENGTH_SHORT).show();
                    srcCell = null;
                    updateTurnTracker();
                } else {
                    showPossibleMoves(moves);
                }
            }//&& srcCell.equals(cellBoard.getCell(xCord, yCord))
            else if (srcCell != null && !cellBoard.isPossibleMove(srcCell.getX(), srcCell.getY(), xCord, yCord) && !srcCellFixed) {
                srcCell = null;
                updatePieces(xCord, yCord);
                updateTurnTracker();
            } else if (!cellBoard.getCell(xCord, yCord).containsPiece() && moveExist(cellBoard.getCell(xCord, yCord)) && srcCell != null) {
                dstCell = cellBoard.getCell(xCord, yCord);
                buttonBoard[dstCell.getX()][dstCell.getY()].setImageResource(getImage(srcCell));
                buttonBoard[srcCell.getX()][srcCell.getY()].setImageResource(R.drawable.blank_square);
                buttonBoard[srcCell.getX()][srcCell.getY()].setVisibility(View.VISIBLE);
                buttonBoard[dstCell.getX()][dstCell.getY()].setVisibility(View.VISIBLE);
                sendMove(new Move(dstCell, srcCell, srcCell.getPlacedPiece()));
            }
        }
    }

    private int getImage(Cell cell) {
        if (cell.getPlacedPiece() != null) {
            if (cell.getPlacedPiece().getColor().equals(Piece.LIGHT)) {
                if (cell.getPlacedPiece().isKing()) {
                    return LIGHT_KING_PIECE_ICON;
                } else {
                    return LIGHT_PIECE_ICON;
                }
            } else {
                if (cell.getPlacedPiece().isKing()) {
                    return DARK_KING_PIECE_ICON;
                } else {
                    return DARK_PIECE_ICON;
                }
            }
        }
        return R.drawable.blank_square;
    }

    private boolean moveExist(Cell cell) {
        for (Move move : moves) {
            if (move.getTo().equals(cell)) {
                return true;
            }
        }
        return false;
    }

    private boolean moveExistFrom(Cell cell) {
        for (Move move : moves) {
            if (move.getFrom().equals(cell)) {
                return true;
            }
        }
        return false;
    }

    public void makeMove(UserDto fromPlayer, UserDto toPlayer, Cell givenSrcCell, Cell givenDstCell) {
        unHighlightPieces();
        boolean captureMove = cellBoard.isCaptureMove(givenSrcCell, givenDstCell);
        if (!cellBoard.isValidMove(givenSrcCell, givenDstCell)) {
            return;
        }
        List<Cell> changedCells = cellBoard.movePiece(givenSrcCell.getCoords(), givenDstCell.getCoords());
        updatePieces(changedCells);
        if (captureMove) {
            moves = cellBoard.getCaptureMoves(givenDstCell);
            if (moves.isEmpty()) {
                this.srcCell = null;
                this.dstCell = null;
                srcCellFixed = false;
                changeTurn(fromPlayer, toPlayer);

            } else {
                this.srcCell = this.dstCell;
                srcCellFixed = true;
                updatePiecePressed(this.srcCell);
                showPossibleMoves(moves);
            }
        } else {
            srcCell = null;
            dstCell = null;
            srcCellFixed = false;
            changeTurn(fromPlayer, toPlayer);
        }
    }

    public int[] getButtonArray() {
        int[] buttons_id = {R.id.button0, R.id.button1, R.id.button2, R.id.button3, R.id.button4,
                R.id.button5, R.id.button6, R.id.button7, R.id.button8,
                R.id.button9, R.id.button10, R.id.button11, R.id.button12, R.id.button13, R.id.button14, R.id.button15,
                R.id.button16, R.id.button17, R.id.button18, R.id.button19, R.id.button20, R.id.button21, R.id.button22,
                R.id.button23, R.id.button24, R.id.button25, R.id.button26, R.id.button27, R.id.button28, R.id.button29, R.id.button30, R.id.button31,
                R.id.button32, R.id.button33, R.id.button34, R.id.button35, R.id.button36, R.id.button37, R.id.button38,
                R.id.button39, R.id.button40, R.id.button41, R.id.button42, R.id.button43, R.id.button44, R.id.button45, R.id.button46, R.id.button47,
                R.id.button48, R.id.button49, R.id.button50, R.id.button51, R.id.button52, R.id.button53, R.id.button54,
                R.id.button55, R.id.button56, R.id.button57, R.id.button58, R.id.button59, R.id.button60, R.id.button61, R.id.button62, R.id.button63};
        return buttons_id;
    }

    public void fillButtonBoard() {
        int index = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                //if ((i + j) % 2 == 0) {
                buttonBoard[i][j] = (ImageButton) findViewById(buttons_id[index]);
                index++;
                buttonBoard[i][j].setTag(i * 10 + j);
                buttonBoard[i][j].setOnTouchListener(listener2);
                buttonBoard[i][j].setOnDragListener(listener3);
                //}
            }
        }
    }


    public void updateBoard(ImageButton[][] buttonIndexes, Board board) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 == 0) {
                    if (!board.getCell(i, j).containsPiece()) {
                        buttonIndexes[i][j].setImageResource(R.drawable.blank_square);
                    } else if (board.getCell(i, j).getPlacedPiece().getColor().equals(Piece.LIGHT)) {
                        if (board.getCell(i, j).getPlacedPiece().isKing()) {
                            buttonIndexes[i][j].setImageResource(LIGHT_KING_PIECE_ICON);
                        } else {
                            buttonIndexes[i][j].setImageResource(LIGHT_PIECE_ICON);
                        }
                    } else if (board.getCell(i, j).getPlacedPiece().getColor().equals(Piece.DARK)) {
                        if (board.getCell(i, j).getPlacedPiece().isKing()) {
                            buttonIndexes[i][j].setImageResource(DARK_KING_PIECE_ICON);
                        } else {
                            buttonIndexes[i][j].setImageResource(DARK_PIECE_ICON);
                        }
                    }
                }
            }
        }
    }

    public void updatePieces(int xCord, int yCord) {
        Cell cell = cellBoard.getCell(xCord, yCord);

        if (cell.getPlacedPiece() != null) {
            if (cell.getPlacedPiece().getColor().equals(Piece.LIGHT)) {
                if (cellBoard.getCell(xCord, yCord).getPlacedPiece().isKing()) {
                    buttonBoard[xCord][yCord].setImageResource(LIGHT_KING_PIECE_ICON);
                } else {
                    buttonBoard[xCord][yCord].setImageResource(LIGHT_PIECE_ICON);
                }
            } else {
                if (cellBoard.getCell(xCord, yCord).getPlacedPiece().isKing()) {
                    buttonBoard[xCord][yCord].setImageResource(DARK_KING_PIECE_ICON);
                } else {
                    buttonBoard[xCord][yCord].setImageResource(DARK_PIECE_ICON);
                }
            }
        }
    }

    public void updatePieces(List<Cell> changedCells) {
        Move possibleMoves;
        for (int i = 0; i < moves.size(); i++) {
            possibleMoves = moves.get(i);
            Cell cell = possibleMoves.getTo();
            if (!cell.containsPiece()) {
                buttonBoard[cell.getX()][cell.getY()].setImageResource(R.drawable.blank_square);
            }
        }
        clearCurrentMove(cellBoard);
        for (Cell cell : changedCells) {
            if (!cell.containsPiece()) {
                buttonBoard[cell.getX()][cell.getY()].setImageResource(R.drawable.blank_square);
            } else if (cell.getPlacedPiece().getColor().equals(Piece.LIGHT)) {
                if (cell.getPlacedPiece().isKing()) {
                    buttonBoard[cell.getX()][cell.getY()].setBackgroundColor(Color.parseColor(LAST_MOVE_COLOR));
                    buttonBoard[cell.getX()][cell.getY()].setImageResource(LIGHT_KING_PIECE_ICON);
//                    buttonBoard[cell.getX()][cell.getY()].setTag(LIGHT_KING_PIECE_ICON);
                } else {
                    buttonBoard[cell.getX()][cell.getY()].setBackgroundColor(Color.parseColor(LAST_MOVE_COLOR));
                    buttonBoard[cell.getX()][cell.getY()].setImageResource(LIGHT_PIECE_ICON);
                }
            } else if (cell.getPlacedPiece().getColor().equals(Piece.DARK)) {
                if (cell.getPlacedPiece().isKing()) {
                    buttonBoard[cell.getX()][cell.getY()].setBackgroundColor(Color.parseColor(LAST_MOVE_COLOR));
                    buttonBoard[cell.getX()][cell.getY()].setImageResource(DARK_KING_PIECE_ICON);
                } else {
                    buttonBoard[cell.getX()][cell.getY()].setBackgroundColor(Color.parseColor(LAST_MOVE_COLOR));
                    buttonBoard[cell.getX()][cell.getY()].setImageResource(DARK_PIECE_ICON);
                }
            }
        }
    }

    public void clearCurrentMove(Board board) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 == 0) {
                    Cell cell = board.getCell(i, j);
                    buttonBoard[cell.getX()][cell.getY()].setBackgroundColor(Color.TRANSPARENT);
                }
            }
        }
    }

    public void updatePiecePressed(Cell givenCell) {
        if (givenCell == null) return;

        givenCell = cellBoard.getCell(givenCell.getX(), givenCell.getY());
        if (currentPlayer.getColor().equals(Piece.LIGHT) && givenCell.getPlacedPiece().getColor().equals(Piece.LIGHT)) {

            if (givenCell.getPlacedPiece().isKing()) {
                buttonBoard[givenCell.getX()][givenCell.getY()].setImageResource(LIGHT_KING_PIECE_PRESSED);
            } else {
                buttonBoard[givenCell.getX()][givenCell.getY()].setImageResource(LIGHT_PIECE_PRESSED);
            }
        }
        if (currentPlayer.getColor().equals(Piece.DARK) && givenCell.getPlacedPiece().getColor().equals(Piece.DARK)) {
            if (cellBoard.getCell(givenCell.getX(), givenCell.getY()).getPlacedPiece().isKing()) {
                buttonBoard[givenCell.getX()][givenCell.getY()].setImageResource(DARK_KING_PIECE_PRESSED);
            } else {
                buttonBoard[givenCell.getX()][givenCell.getY()].setImageResource(DARK_PIECE_PRESSED);
            }
        }
    }

    public void changeTurn(UserDto fromPlayer, UserDto toPlayer) {
        setPlayerTime(fromPlayer, toPlayer);
        this.currentPlayer = toPlayer;
        updateTurnTracker();
    }

    public void setPlayerTime(UserDto fromPlayer, UserDto toPlayer) {
        Long toPlayerMinutes = toPlayer.getSecondsRemaining() / 60;
        Long toPlayerSeconds = toPlayer.getSecondsRemaining() - toPlayerMinutes * 60;

        Long fromPlayerMinutes = fromPlayer.getSecondsRemaining() / 60;
        Long fromPlayerSeconds = fromPlayer.getSecondsRemaining() - fromPlayerMinutes * 60;


        if (fromPlayer.getUserId().equals(LocalCache.getInstance().getString(USER_ID, this))) {
            player1Time.setCenterTitle(toPlayerMinutes.toString() + ":" + twoDigits(toPlayerSeconds.toString()));
            player2Time.setCenterTitle(fromPlayerMinutes.toString() + ":" + twoDigits(fromPlayerSeconds.toString()));
        } else {
            player2Time.setCenterTitle(toPlayerMinutes.toString() + ":" + twoDigits(toPlayerSeconds.toString()));
            player1Time.setCenterTitle(fromPlayerMinutes.toString() + ":" + twoDigits(fromPlayerSeconds.toString()));
        }

        Integer progress = 100 * fromPlayer.getSecondsRemaining().intValue() / (gameMaxTime * 60);
        if (toPlayer.getUserId().equals(LocalCache.getInstance().getString(USER_ID, this))) {
            player1Time.setProgressValue(progress);
        } else {
            player2Time.setProgressValue(progress);
        }


        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(toPlayer.getSecondsRemaining() * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                onTimerTick(millisUntilFinished, fromPlayer);
            }

            public void onFinish() {
            }
        }.start();
    }

    public void onTimerTick(long millisUntilFinished, UserDto fromPlayer) {
        Long remainingSeconds = millisUntilFinished / 1000;
        Long toPlayerMinutes = remainingSeconds / 60;
        Long toPlayerSeconds = remainingSeconds - toPlayerMinutes * 60;
        Integer progress = 100 * remainingSeconds.intValue() / (gameMaxTime * 60);
        if (fromPlayer.getUserId().equals(LocalCache.getInstance().getString(USER_ID, this))) {
            player1Time.setProgressValue(progress);
            player1Time.setCenterTitle(toPlayerMinutes.toString() + ":" + twoDigits(toPlayerSeconds.toString()));
        } else {
            player2Time.setProgressValue(progress);
            player2Time.setCenterTitle(toPlayerMinutes.toString() + ":" + twoDigits(toPlayerSeconds.toString()));
        }
    }

    public void unHighlightPieces() {
        Cell highlightedCell;
        while (!highlightedCells.isEmpty()) {
            highlightedCell = highlightedCells.remove(0);
            if (highlightedCell.getPlacedPiece().getColor().equals(Piece.LIGHT)) {
                if (highlightedCell.getPlacedPiece().isKing()) {
                    buttonBoard[highlightedCell.getX()][highlightedCell.getY()].setImageResource(LIGHT_KING_PIECE_ICON);
                } else {
                    buttonBoard[highlightedCell.getX()][highlightedCell.getY()].setImageResource(LIGHT_PIECE_ICON);
                }
            } else {
                if (highlightedCell.getPlacedPiece().isKing()) {
                    buttonBoard[highlightedCell.getX()][highlightedCell.getY()].setImageResource(DARK_KING_PIECE_ICON);
                } else {
                    buttonBoard[highlightedCell.getX()][highlightedCell.getY()].setImageResource(DARK_PIECE_ICON);
                }
            }
        }
    }

    public void updateTurnTracker() {
        if (isMe(this.currentPlayer)) {
            List<Cell> currentPlayerPieces = cellBoard.getCellPieces(this.currentPlayer.getColor());
            List<Move> moves = cellBoard.possibleMoves(currentPlayerPieces);
            for (Move cell : moves) {
                if (!moves.isEmpty()) {
                    if (cell.getPiece().getColor().equals(Piece.DARK) && cell.getPiece().isKing()) {
                        buttonBoard[cell.getFrom().getX()][cell.getFrom().getY()].setImageResource(DARK_KING_PIECE_HIGHLIGHTED);
                    } else if (cell.getPiece().getColor().equals(Piece.DARK)) {
                        buttonBoard[cell.getFrom().getX()][cell.getFrom().getY()].setImageResource(DARK_PIECE_HIGHLIGHTED);
                    } else if (cell.getPiece().getColor().equals(Piece.LIGHT) && cell.getPiece().isKing()) {
                        buttonBoard[cell.getFrom().getX()][cell.getFrom().getY()].setImageResource(LIGHT_KING_PIECE_HIGHLIGHTED);
                    } else if (cell.getPiece().getColor().equals(Piece.LIGHT)) {
                        buttonBoard[cell.getFrom().getX()][cell.getFrom().getY()].setImageResource(LIGHT_PIECE_HIGHLIGHTED);
                    }
                    highlightedCells.add(cell.getFrom());
                }
            }
        }
    }

    public void clearPossibleMoves() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 == 0) {
                    Cell cell = cellBoard.getCell(i, j);
                    if (!cell.containsPiece()) {
                        buttonBoard[cell.getX()][cell.getY()].setImageResource(R.drawable.blank_square);
                    }
                }
            }
        }
    }

    public void showPossibleMoves(List<Move> moves) {
        for (int i = 0; i < moves.size(); i++) {
            Cell cell = moves.get(i).getTo();
            if (!cell.containsPiece()) {
                buttonBoard[cell.getX()][cell.getY()].setImageResource(R.drawable.blank_square);
            }
        }

        for (Move cell : moves) {
            if (srcCell != null && srcCell.equals(cell.getFrom())) {
                buttonBoard[cell.getTo().getX()][cell.getTo().getY()].setImageResource(POSSIBLE_MOVE);
                srcCell = cellBoard.getCell(cell.getFrom().getX(), cell.getFrom().getY());

                updatePiecePressed(srcCell);
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            this.resizeBoardToScreenSizePortrait();
        }
//		else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
//			//this.resizeBoardToScreenSizeLandscape();
//		}
    }

    public void resizeBoardToScreenSizePortrait() {
        WindowManager wm = (WindowManager) this.getApplication().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        double width = metrics.widthPixels;

        ImageView imageView = (ImageView) findViewById(R.id.boardImageView);
        ViewGroup.LayoutParams imageParams = imageView.getLayoutParams();
        imageParams.width = (int) (width * 1.0028);
        imageParams.height = (int) (width * 1.0085);
        imageView.setLayoutParams(imageParams);

        // Sets the width & height for the grid of game buttons in the layout
        LinearLayout buttonLayout = (LinearLayout) findViewById(R.id.parent_layout);
        ViewGroup.LayoutParams buttonLayoutParams = buttonLayout.getLayoutParams();     // Gets the layout params that will allow you to resize the layout
        buttonLayoutParams.width = (int) (width * 1.017);
        buttonLayoutParams.height = (int) (width * 1.0123);
        buttonLayout.setLayoutParams(buttonLayoutParams);
    }

    public void resizeBoardToScreenSizeLandscape() {
        final TypedArray styledAttributes = getApplicationContext().getTheme().obtainStyledAttributes(
                new int[]{android.R.attr.actionBarSize});
        int actionBarHeight = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        WindowManager wm = (WindowManager) this.getApplication().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        double height = metrics.heightPixels - (actionBarHeight * 1.75);

        ImageView imageView = (ImageView) findViewById(R.id.boardImageView);
        ViewGroup.LayoutParams imageParams = imageView.getLayoutParams();
        imageParams.width = (int) (height * 1.0028);
        imageParams.height = (int) (height * 1.0085);
        imageView.setLayoutParams(imageParams);

        LinearLayout buttonLayout = (LinearLayout) findViewById(R.id.parent_layout);
        ViewGroup.LayoutParams buttonLayoutParams = buttonLayout.getLayoutParams();
        buttonLayoutParams.width = (int) (height * 0.967);
        buttonLayoutParams.height = (int) (height * 0.9723);
        buttonLayout.setLayoutParams(buttonLayoutParams);
    }

    @Override
    public void onBackPressed() {
    }

    public boolean isMe(UserDto player) {
        return this.currentPlayer != null && player.getUserId().equals(LocalCache.getInstance().getString(USER_ID, this));
    }

    public AudioPlayer getAudioPlayer() {
        return audioPlayer;
    }

    public void showNoInternetDialog(boolean show) {
        runOnUiThread(() -> {

            if (noInternetDialog == null) return;
            if (!show) {
                noInternetDialog.dismiss();
            }
            if (show && optionsDialog != null && optionsDialog.isShowing()) {
                showGameOptionsDialog(false);
            }
            if (show && !noInternetDialog.isShowing()) {
                noInternetDialog.show();
            }
        });
    }

    public void handleReconnection() {
        showNoInternetDialog(false);
//        HomeController.getInstance().addTopic(true);
        gameController.getGameState();
    }

    public void handleConnectionLose() {
        showNoInternetDialog(true);
    }

    @Override
    public void onConnectionStatusChange(boolean isConnected) {
        this.isConnected = isConnected;
        gameController.setConnected(isConnected);
        if (isConnected) {
            handleReconnection();
        } else {
            handleConnectionLose();
        }
    }
}
