package games.tetris.engine.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Entry point for Tetris components that will schedule commands on the engine and get notified about the execution.
 * 
 * @author edwin
 *
 */
public class TetrisGame {

	private final TetrisCommandProcessor tetrisCommandProcessor;
	private final TetrisGameState tetrisGameState;
	private final Collection<AbstractTetrisGameSubscriber> subscribers;

	/**
	 * Create a new Tetris game. Subscribers can play the game by placing {@code MoveActions} and act on the events provided by the command processor.
	 */
	public TetrisGame() {
		this.tetrisCommandProcessor = new TetrisCommandProcessor();
		this.tetrisGameState = new ThreadSafeTetrisGameState();
		this.subscribers = Collections.synchronizedList(new ArrayList<>());
	}

	/**
	 * Subscribe a component to the Tetris game. After subscription, the component can access the game state and place commands.
	 *
	 */
	public void subscribe(final AbstractTetrisGameSubscriber tetrisGameSubscriber) {
		if (tetrisGameSubscriber == null) {
			throw new IllegalArgumentException("TetrisGameSubscriber is null");
		}

		tetrisGameSubscriber.setCommandProcessor(this.tetrisCommandProcessor);
		tetrisGameSubscriber.setTetrisGameState(this.tetrisGameState);

		// Add it to the subscribers so that it can be unsubscribed when the game is cleaned up.
		this.subscribers.add(tetrisGameSubscriber);
	}

	public void unsubscribe(final AbstractTetrisGameSubscriber tetrisGameSubscriber) {
		if (tetrisGameSubscriber == null) {
			throw new IllegalArgumentException("TetrisGameSubscriber is null");
		}

		tetrisGameSubscriber.setCommandProcessor(null);
		tetrisGameSubscriber.setTetrisGameState(null);

		// Remove the subscriber explicitly from the known subscribers
		this.subscribers.remove(tetrisGameSubscriber);
	}

	/**
	 * Method that should be called when the game has ended to clean up resources.
	 */
	public void shutdown() {
		for (AbstractTetrisGameSubscriber subscriber : this.subscribers) {
			subscriber.setCommandProcessor(null);
			subscriber.setTetrisGameState(null);
		}
		this.subscribers.clear();

		this.tetrisCommandProcessor.shutdown();
	}

	/**
	 * Check if the game is shut down.
	 * 
	 * @return {@code true} when the game is currently shut down.
	 * 
	 * @throws IllegalStateException when the game has not been properly shut down.
	 */
	public boolean isShutdown() {
		boolean shutdown = this.tetrisCommandProcessor.isShutdown();
		if (shutdown && this.subscribers.size() > 0) {
			throw new IllegalStateException("Invalid shutdown state: Number is subscribers is greater than 0: " + this.subscribers.size());
		}
		return shutdown;
	}

	/**
	 * @return An unmodifiable {@code Collection} that contains the current subscribers.
	 */
	public Collection<AbstractTetrisGameSubscriber> getSubscribers() {
		return Collections.unmodifiableCollection(this.subscribers);
	}
}