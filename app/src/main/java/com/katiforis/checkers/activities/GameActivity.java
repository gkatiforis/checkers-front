package com.katiforis.checkers.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.katiforis.checkers.DTO.UserDto;
import com.katiforis.checkers.DTO.response.GameState;
import com.katiforis.checkers.DTO.PlayerAnswer;
import com.katiforis.checkers.R;
import com.katiforis.checkers.conf.Const;
import com.katiforis.checkers.controller.GameController;
import com.katiforis.checkers.game.Board;
import com.katiforis.checkers.game.Cell;
import com.katiforis.checkers.game.Move;
import com.katiforis.checkers.game.Piece;

import com.katiforis.checkers.adapter.PlayerAdapter;
import com.katiforis.checkers.util.LocalCache;

import tyrantgit.explosionfield.ExplosionField;

import static com.katiforis.checkers.util.CachedObjectProperties.CURRENT_GAME_ID;
import static com.katiforis.checkers.util.CachedObjectProperties.USER_ID;

public class GameActivity extends AppCompatActivity {

    public static GameActivity INSTANCE;

	ProgressBar mProgressBar;
	CountDownTimer mCountDownTimer;
	int timerStep=0;
	long dateStarted;
	long dateCurrent;
	ExplosionField explosionField;

	private RecyclerView userRecyclerView;
	private PlayerAdapter playerAdapter;
	private List<UserDto> userList = new ArrayList<>();

	private GameController gameController;
	public static boolean populated;

	private int[] buttons_id;
	private Button[][] buttonBoard;
	private List<Cell> highlightedCells;
	private List<Move> moves;
	private UserDto player1, player2, currentPlayer;
	private boolean srcCellFixed;
	private Board cellBoard = new Board();
	private Cell srcCell, dstCell;

	public void setGameState(GameState gamestate){
		runOnUiThread(() -> {
				Log.i(Const.TAG, "GameState：" + gamestate);
				setPlayerList(gamestate.getPlayers());
				dateStarted = gamestate.getDateStarted().getTime();
				dateCurrent = gamestate.getCurrentDate().getTime();

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

			this.player1 = gamestate.getPlayers().get(0);
			this.player2 = gamestate.getPlayers().get(1);
			this.currentPlayer = gamestate.getCurrentPlayer();

			updateTurnTracker();

			setBoard(gamestate.getBoard());

			player1.setUsername(player1.getColor() + player1.getUsername());
			player2.setUsername(player2.getColor() + player2.getUsername());
			if(this.player1.getUserId().equals(LocalCache.getInstance().getString(USER_ID))){
				player1.setUsername("Q" + player1.getUsername());
			}else{
				player2.setUsername("Q" + player2.getUsername());
			}

			if(this.player1.getUserId().equals(LocalCache.getInstance().getString(USER_ID)) &&
					player1.getColor().equals(Piece.LIGHT)){
				FrameLayout mView = findViewById(R.id.gameboard);
				mView.setRotation(180);
				ViewGroup.LayoutParams lp = mView.getLayoutParams();
			}else if(this.player2.getUserId().equals(LocalCache.getInstance().getString(USER_ID)) &&
					player2.getColor().equals(Piece.LIGHT)){
				FrameLayout mView = findViewById(R.id.gameboard);
				mView.setRotation(180);
				ViewGroup.LayoutParams lp = mView.getLayoutParams();
			}

		});
	}

	void initComponents(){
		getSupportActionBar().hide();
		explosionField = ExplosionField.attach2Window(this);
		mProgressBar=(ProgressBar)findViewById(R.id.progressBar);


        userRecyclerView = findViewById(R.id.my_recycler_viewH);
      //  userRecyclerView.addItemDecoration(new DividerItemDecoration(GameActivity.this, LinearLayoutManager.HORIZONTAL));
        playerAdapter = new PlayerAdapter(userList, getApplicationContext());
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(GameActivity.this, LinearLayoutManager.HORIZONTAL, false);
        userRecyclerView.setLayoutManager(horizontalLayoutManager);
        userRecyclerView.setAdapter(playerAdapter);

//		viewKonfetti = (KonfettiView)findViewById(R.id.viewKonfetti);
////		viewKonfetti.build()
////				.addColors(Color.RED, Color.GREEN)
////				.setSpeed(1f, 5f)
////				.setFadeOutEnabled(true)
////				.setTimeToLive(2000L)
////				.addShapes(Shape.RECT, Shape.CIRCLE)
////				.setPosition(0f, -359f, -359f, 0f)
////				.stream(200, 5000L);
//
//		final KonfettiView konfettiView = (KonfettiView)findViewById(R.id.viewKonfetti);
//		konfettiView.build()
//				.addColors(Color.RED, Color.GREEN)
//				.setSpeed(1f, 5f)
//				.setFadeOutEnabled(true)
//				.setTimeToLive(2000L)
//				.addShapes(Shape.RECT, Shape.CIRCLE)
//				.setPosition(0f, -359f, -359f, 0f)
//				.stream(200, 5000L);

    }


	private void updatePlayerScore(String username, String points) {
		for (UserDto player :userList){
			if(player.getUsername().equals(username)){
				player.getPlayerDetails().setElo(player.getPlayerDetails().getElo() + Integer.valueOf(points));
			}
		}
		playerAdapter.notifyDataSetChanged();
	}

	void setPlayerList(List<UserDto> players){
		userList.clear();
		userList.addAll(players);
		playerAdapter.notifyDataSetChanged();
	}

	public void sendMove(Move move){
		PlayerAnswer playerAnswer = new PlayerAnswer();
		playerAnswer.setMove(move);
		gameController.sendAnswer(LocalCache.getInstance().getString(CURRENT_GAME_ID), playerAnswer);
	}

	public void makeMove(PlayerAnswer playerAnswer){
		runOnUiThread(() -> {
			makeMove(playerAnswer.getCurrentPlayer(), playerAnswer.getMove().getFrom(), playerAnswer.getMove().getTo());
		});
	}

	private void setBoard(Board board){
		this.cellBoard = board;
		updateBoard(buttonBoard, cellBoard);
	}

	@SuppressLint("CheckResult")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_layout);
		initComponents();
		INSTANCE = this;
		populated = false;
		gameController = GameController.getInstance();
		gameController.setGameActivity(this);
		if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
			this.resizeBoardToScreenSizePortrait();
		}
		else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
			this.resizeBoardToScreenSizeLandscape();
		}
		srcCell = null;
		dstCell = null;
		srcCellFixed = false;
		highlightedCells = new ArrayList<>();
		buttons_id = getButtonArray();
		buttonBoard = new Button[8][8];
		fillButtonBoard(listener);
		this.moves = new ArrayList<>();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(mCountDownTimer != null){
			mCountDownTimer.cancel();
			mCountDownTimer = null;
			timerStep=0;
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		String gameId = LocalCache.getInstance().getString(CURRENT_GAME_ID);
		if (gameId == null || gameId.length() == 0) {
			return;
		}
		gameController.getGameState(gameId);
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	private View.OnClickListener listener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int tag = (Integer) v.getTag();
			int xCord = tag / 10;
			int yCord = tag % 10;

			if(currentPlayer.getUserId().equals(LocalCache.getInstance().getString(USER_ID))){
				playerTurn(xCord, yCord);
			}
		}
	};

	public void playerTurn(int xCord, int yCord){

		if (cellBoard.hasMoves(Piece.DARK) && cellBoard.hasMoves(Piece.LIGHT)) {
			if (cellBoard.getCell(xCord, yCord).containsPiece() && cellBoard.getCell(xCord, yCord).getPlacedPiece().getColor().equals(currentPlayer.getColor()) && srcCell == null) {
				unHighlightPieces();
				srcCell = cellBoard.getCell(xCord, yCord);

				List<Cell> currentPlayerPieces = cellBoard.getCellPieces(this.currentPlayer.getColor());
				moves = cellBoard.possibleMoves(currentPlayerPieces);


				if(!moveExistFrom(srcCell)){
					srcCell = null;
					updatePieces(xCord, yCord);
					updateTurnTracker();
					return;
				}
				if (moves.isEmpty()) {
					Toast.makeText(getApplicationContext(), "No possible moves!", Toast.LENGTH_SHORT).show();
					srcCell = null;
					updateTurnTracker();
				}
				else {
					showPossibleMoves(moves);
				}
			}
			else if (srcCell != null && srcCell.equals(cellBoard.getCell(xCord, yCord)) && !srcCellFixed) {
				srcCell = null;
				updatePieces(xCord, yCord);
				updateTurnTracker();
			} else if (!cellBoard.getCell(xCord, yCord).containsPiece() && moveExist(cellBoard.getCell(xCord, yCord)) && srcCell != null) {
				dstCell = cellBoard.getCell(xCord, yCord);
				sendMove(new Move(dstCell, srcCell, srcCell.getPlacedPiece()));

			}
		}
	}

	private boolean moveExist(Cell cell){
		for(Move move:moves){
			if(move.getTo().equals(cell)){
				return true;
			}
		}
		return false;
	}

	private boolean moveExistFrom(Cell cell){
		for(Move move:moves){
			if(move.getFrom().equals(cell)){
				return true;
			}
		}
		return false;
	}

	public void makeMove(UserDto currentPlayer, Cell givenSrcCell, Cell givenDstCell) {
		unHighlightPieces();
		boolean captureMove = cellBoard.isCaptureMove(givenSrcCell, givenDstCell);
		List<Cell> changedCells = cellBoard.movePiece(givenSrcCell.getCoords(), givenDstCell.getCoords());
		updatePieces(changedCells);
		if (captureMove) {
			moves = cellBoard.getCaptureMoves(givenDstCell);
			if (moves.isEmpty()) {
				this.srcCell = null;
				this.dstCell = null;
				srcCellFixed = false;
				changeTurn(currentPlayer);

			}
			else {
				this.srcCell = this.dstCell;
				srcCellFixed = true;
				updatePiecePressed(this.srcCell);
				showPossibleMoves(moves);
			}
		}
		else {
			srcCell = null;
			dstCell = null;
			srcCellFixed = false;
			changeTurn(currentPlayer);
		}
	}

	public int[] getButtonArray(){
		int[] buttons_id = {R.id.button0, R.id.button2, R.id.button4, R.id.button6,
				R.id.button9, R.id.button11, R.id.button13, R.id.button15,
				R.id.button16, R.id.button18, R.id.button20, R.id.button22,
				R.id.button25, R.id.button27, R.id.button29, R.id.button31,
				R.id.button32, R.id.button34, R.id.button36, R.id.button38,
				R.id.button41, R.id.button43, R.id.button45, R.id.button47,
				R.id.button48, R.id.button50, R.id.button52, R.id.button54,
				R.id.button57, R.id.button59, R.id.button61, R.id.button63};
		return buttons_id;
	}

	public void fillButtonBoard(View.OnClickListener listener) {
		int index = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if ((i + j) % 2 == 0) {
					buttonBoard[i][j] = (Button) findViewById(buttons_id[index]);
					index++;
					buttonBoard[i][j].setTag(i * 10 + j);
					buttonBoard[i][j].setOnClickListener(listener);
				}
			}
		}
	}

	public void updateBoard(Button[][] buttonIndexes, Board board) {
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if ((i + j) % 2 == 0) {
					if (!board.getCell(i, j).containsPiece()) {
						buttonIndexes[i][j].setBackgroundResource(R.drawable.blank_square);
					}
					else if (board.getCell(i, j).getPlacedPiece().getColor().equals(Piece.LIGHT)) {
						if (board.getCell(i, j).getPlacedPiece().isKing()) {
							buttonIndexes[i][j].setBackgroundResource(R.drawable.light_king_piece);
						}
						else {
							buttonIndexes[i][j].setBackgroundResource(R.drawable.light_piece);
						}
					}
					else if (board.getCell(i, j).getPlacedPiece().getColor().equals(Piece.DARK)) {
						if (board.getCell(i, j).getPlacedPiece().isKing()) {
							buttonIndexes[i][j].setBackgroundResource(R.drawable.dark_king_piece);
						}
						else {
							buttonIndexes[i][j].setBackgroundResource(R.drawable.dark_piece);
						}
					}
				}
			}
		}
	}

	public void updatePieces(int xCord, int yCord) {
		Move possMoves;
		for (int i = 0; i < moves.size(); i++) {
			possMoves = moves.get(i);
			buttonBoard[possMoves.getTo().getX()][possMoves.getTo().getY()].setBackgroundResource(R.drawable.blank_square);
		}
//		&& cellBoard.getCell(xCord, yCord).containsPiece()
		if (cellBoard.getCell(xCord, yCord).getPlacedPiece().getColor().equals(Piece.LIGHT)) {
			if (cellBoard.getCell(xCord, yCord).getPlacedPiece().isKing()) {
				buttonBoard[xCord][yCord].setBackgroundResource(R.drawable.light_king_piece);
			}
			else {
				buttonBoard[xCord][yCord].setBackgroundResource(R.drawable.light_piece);
			}
		}
		else {
			if (cellBoard.getCell(xCord, yCord).getPlacedPiece().isKing()) {
				buttonBoard[xCord][yCord].setBackgroundResource(R.drawable.dark_king_piece);
			}
			else {
				buttonBoard[xCord][yCord].setBackgroundResource(R.drawable.dark_piece);
			}
		}
	}

	public void updatePieces(List<Cell> changedCells) {
		Move possMoves;
		for (int i = 0; i < moves.size(); i++) {
			possMoves = moves.get(i);
			buttonBoard[possMoves.getTo().getX()][possMoves.getTo().getY()].setBackgroundResource(R.drawable.blank_square);   // color possible moves blank
		}

		for (Cell cell : changedCells) {
			if (!cell.containsPiece()) {
				buttonBoard[cell.getX()][cell.getY()].setBackgroundResource(R.drawable.blank_square);
			} else if (cell.getPlacedPiece().getColor().equals(Piece.LIGHT)) {
				if (cell.getPlacedPiece().isKing()) {
					buttonBoard[cell.getX()][cell.getY()].setBackgroundResource(R.drawable.light_king_piece);
				} else {
					buttonBoard[cell.getX()][cell.getY()].setBackgroundResource(R.drawable.light_piece);
				}
			} else if (cell.getPlacedPiece().getColor().equals(Piece.DARK)) {
				if (cell.getPlacedPiece().isKing()) {
					buttonBoard[cell.getX()][cell.getY()].setBackgroundResource(R.drawable.dark_king_piece);
				} else {
					buttonBoard[cell.getX()][cell.getY()].setBackgroundResource(R.drawable.dark_piece);
				}
			}
		}
	}

	public void updatePiecePressed(Cell givenCell) {
		if(givenCell == null)return;

		givenCell = cellBoard.getCell(givenCell.getX(), givenCell.getY());
		if (currentPlayer.getColor().equals(Piece.LIGHT) && givenCell.getPlacedPiece().getColor().equals(Piece.LIGHT)) {

			if (givenCell.getPlacedPiece().isKing()) {
				buttonBoard[givenCell.getX()][givenCell.getY()].setBackgroundResource(R.drawable.light_king_piece_pressed);
			}
			else {
				buttonBoard[givenCell.getX()][givenCell.getY()].setBackgroundResource(R.drawable.light_piece_pressed);
			}
		}
		if (currentPlayer.getColor().equals(Piece.DARK) && givenCell.getPlacedPiece().getColor().equals(Piece.DARK)) {
			if (cellBoard.getCell(givenCell.getX(), givenCell.getY()).getPlacedPiece().isKing()) {
				buttonBoard[givenCell.getX()][givenCell.getY()].setBackgroundResource(R.drawable.dark_king_piece_pressed);
			}
			else {
				buttonBoard[givenCell.getX()][givenCell.getY()].setBackgroundResource(R.drawable.dark_piece_pressed);
			}
		}
	}

	public void changeTurn(UserDto player) {
		this.currentPlayer = player;
		updateTurnTracker();
	}

	public void unHighlightPieces() {
		Cell highlightedCell;
		while (!highlightedCells.isEmpty()) {
			highlightedCell = highlightedCells.remove(0);
			if (highlightedCell.getPlacedPiece().getColor().equals(Piece.LIGHT)) {
				if (highlightedCell.getPlacedPiece().isKing()) {
					buttonBoard[highlightedCell.getX()][highlightedCell.getY()].setBackgroundResource(R.drawable.light_king_piece);
				} else {
					buttonBoard[highlightedCell.getX()][highlightedCell.getY()].setBackgroundResource(R.drawable.light_piece);
				}
			} else {
				if (highlightedCell.getPlacedPiece().isKing()) {
					buttonBoard[highlightedCell.getX()][highlightedCell.getY()].setBackgroundResource(R.drawable.dark_king_piece);
				} else {
					buttonBoard[highlightedCell.getX()][highlightedCell.getY()].setBackgroundResource(R.drawable.dark_piece);
				}
			}
		}
	}

	public void updateTurnTracker() {
		if(this.currentPlayer != null) {
			// Get all the pieces of the current player that can move & highlight them
			List<Cell> currentPlayerPieces = cellBoard.getCellPieces(this.currentPlayer.getColor());
			List<Move> moves = cellBoard.possibleMoves(currentPlayerPieces);
			for (Move cell : moves) {
				if (!moves.isEmpty()) {
					if (cell.getPiece().getColor().equals(Piece.DARK) && cell.getPiece().isKing()) {
						buttonBoard[cell.getFrom().getX()][cell.getFrom().getY()].setBackgroundResource(R.drawable.dark_king_highlighted);
					} else if (cell.getPiece().getColor().equals(Piece.DARK)) {
						buttonBoard[cell.getFrom().getX()][cell.getFrom().getY()].setBackgroundResource(R.drawable.dark_piece_highlighted);
					} else if (cell.getPiece().getColor().equals(Piece.LIGHT) && cell.getPiece().isKing()) {
						buttonBoard[cell.getFrom().getX()][cell.getFrom().getY()].setBackgroundResource(R.drawable.light_king_highlighted);
					} else if (cell.getPiece().getColor().equals(Piece.LIGHT)) {
						buttonBoard[cell.getFrom().getX()][cell.getFrom().getY()].setBackgroundResource(R.drawable.light_piece_highlighted);
					}
					highlightedCells.add(cell.getFrom());
				}
			}
		}
	}

	public void showPossibleMoves(List<Move> moves) {
		for (Move cell : moves) {
			if(srcCell != null && srcCell.equals(cell.getFrom())){
				buttonBoard[cell.getTo().getX()][cell.getTo().getY()].setBackgroundResource(R.drawable.possible_moves_image);
				srcCell = cellBoard.getCell(cell.getFrom().getX(), cell.getFrom().getY());
				updatePiecePressed(srcCell);
			}

		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
			this.resizeBoardToScreenSizePortrait();
		}
		else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
			this.resizeBoardToScreenSizeLandscape();
		}
	}

	public void resizeBoardToScreenSizePortrait(){
		// Gets the width of the current screen
		WindowManager wm = (WindowManager) this.getApplication().getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);
		double width = metrics.widthPixels;

		// Sets the width & height for the game board image
		ImageView imageView = (ImageView) findViewById(R.id.boardImageView);
		ViewGroup.LayoutParams imageParams = imageView.getLayoutParams();
		imageParams.width =  (int) (width * 1.0028);
		imageParams.height = (int) (width * 1.0085);
		imageView.setLayoutParams(imageParams);

		// Sets the width & height for the grid of game buttons in the layout
		LinearLayout buttonLayout = (LinearLayout) findViewById(R.id.parent_layout);
		ViewGroup.LayoutParams buttonLayoutParams = buttonLayout.getLayoutParams();     // Gets the layout params that will allow you to resize the layout
		buttonLayoutParams.width =  (int) (width * 0.967);
		buttonLayoutParams.height = (int) (width * 0.9723);
		buttonLayout.setLayoutParams(buttonLayoutParams);
	}

	public void resizeBoardToScreenSizeLandscape(){
		final TypedArray styledAttributes = getApplicationContext().getTheme().obtainStyledAttributes(
				new int[] { android.R.attr.actionBarSize });
		int actionBarHeight = (int) styledAttributes.getDimension(0, 0);
		styledAttributes.recycle();

		WindowManager wm = (WindowManager) this.getApplication().getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);
		double height = metrics.heightPixels - (actionBarHeight * 1.75);    // subtract size of top action bar so it doesn't partially hide board

		ImageView imageView = (ImageView) findViewById(R.id.boardImageView);
		ViewGroup.LayoutParams imageParams = imageView.getLayoutParams();
		imageParams.width =  (int) (height * 1.0028);
		imageParams.height = (int) (height * 1.0085);
		imageView.setLayoutParams(imageParams);

		LinearLayout buttonLayout = (LinearLayout) findViewById(R.id.parent_layout);
		ViewGroup.LayoutParams buttonLayoutParams = buttonLayout.getLayoutParams();     // Gets the layout params that will allow you to resize the layout
		buttonLayoutParams.width =  (int) (height * 0.967);
		buttonLayoutParams.height = (int) (height * 0.9723);
		buttonLayout.setLayoutParams(buttonLayoutParams);
	}

	@Override
	public void onBackPressed() {}
}
