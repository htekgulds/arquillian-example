package tr.gov.tuik.arquillian.jpa;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.*;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by studentA on 7/28/2016.
 */
@RunWith(Arquillian.class)
public class GameTest {

    @Deployment
    public static Archive<WebArchive> createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addPackage(Game.class.getPackage())
                .addAsResource("test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    private static final String[] GAME_TITLES = {
            "Super Mario Brothers",
            "Mario Kart",
            "F-Zero"
    };

    @PersistenceContext
    EntityManager em;

    @Inject
    UserTransaction utx;

    @Test
    public void shouldFindAllGamesUsingJpqlQuery() {
        String fetchingAllGamesInJpql = "select g from Game g order by g.id";

        System.out.println("Selecting (using JPQL)...");
        List<Game> games = em.createQuery(fetchingAllGamesInJpql, Game.class).getResultList();

        System.out.println("Found " + games.size() + " games (using JQPL):");
        assertContainsAllGames(games);
    }

    private void assertContainsAllGames(Collection<Game> retrievedGames) {
        Assert.assertEquals(GAME_TITLES.length, retrievedGames.size());
        final Set<String> retrievedGameTitles = new HashSet<>();
        for (Game game : retrievedGames) {
            System.out.println("* " + game);
            retrievedGameTitles.add(game.getTitle());
        }
        Assert.assertTrue(retrievedGameTitles.containsAll(Arrays.asList(GAME_TITLES)));
    }

    @Before
    public void preparePersistenceTest() throws HeuristicRollbackException, RollbackException, HeuristicMixedException, SystemException, NotSupportedException {
        clearData();
        insertData();
        startTransaction();
    }

    @After
    public void commitTransaction() throws HeuristicRollbackException, RollbackException, HeuristicMixedException, SystemException {
        utx.commit();
    }

    private void clearData() throws HeuristicRollbackException, RollbackException, HeuristicMixedException, SystemException, NotSupportedException {
        utx.begin();
        em.joinTransaction();
        System.out.println("Dumping old records...");
        em.createQuery("delete from Game").executeUpdate();
        utx.commit();
    }

    private void insertData() throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        utx.begin();
        em.joinTransaction();
        System.out.println("Inserting records...");
        for (String title : GAME_TITLES) {
            Game game = new Game(title);
            em.persist(game);
        }
        utx.commit();
        em.clear();
    }

    private void startTransaction() throws SystemException, NotSupportedException {
        utx.begin();
        em.joinTransaction();
    }

}
