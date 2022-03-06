package android.com.viper.ui.userDetail;

import android.com.viper.model.network.interceptor.NetworkAvailabilityMonitor;
import android.com.viper.model.response.UserDetailModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import rx.Subscriber;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class GitHubUserDetailPresenterTest {

  @Mock private GitHubUserDetailInteractor gitHubUserDetailInteractor;
  @Mock private NetworkAvailabilityMonitor networkAvailabilityMonitor;
  @Mock private GitHubUserDetailViewCallBacks view;

  private GitHubUserDetailPresenter presenter;

  @Before public void setUp() {
    MockitoAnnotations.initMocks(this);
    presenter = new GitHubUserDetailPresenter(gitHubUserDetailInteractor, networkAvailabilityMonitor);
    presenter.takeGitHubUserName("TheAlgorithms");
  }

  @Test @SuppressWarnings("unchecked") public void testTakeViewInteractorInvoked() {
    //When
    presenter.takeView(view);
    //Then
    Mockito.verify(gitHubUserDetailInteractor).execute(ArgumentMatchers.any(Subscriber.class),
        ArgumentMatchers.anyString());
  }

  @Test @SuppressWarnings("unchecked") public void testTakeViewCorrectCallbacksPassed() {
    //Given
    presenter = Mockito.spy(presenter);
    ArgumentCaptor<Subscriber> subscriber = ArgumentCaptor.forClass(Subscriber.class);
    //When
    presenter.takeView(view);
    //Then
    Mockito.verify(gitHubUserDetailInteractor).execute(subscriber.capture(), ArgumentMatchers.anyString());
    Mockito.verify(presenter, Mockito.never()).onUserDetailsReceived(ArgumentMatchers.any(UserDetailModel.class));
    Mockito.verify(presenter, Mockito.never()).onUserDetailError(ArgumentMatchers.anyString());
    subscriber.getValue().onNext(Mockito.mock(UserDetailModel.class));
    Mockito.verify(presenter).onUserDetailsReceived(ArgumentMatchers.any(UserDetailModel.class));
    subscriber.getValue().onError(Mockito.mock(Throwable.class));
    Mockito.verify(presenter).onUserDetailError(ArgumentMatchers.anyString());
  }

  @Test public void testGitHubUserDetailViewUpdated() {
    //Given
    UserDetailModel userDetailModel = mock(UserDetailModel.class);
    presenter.takeView(view);
    //When
    presenter.onUserDetailsReceived(userDetailModel);
    //Then
    verify(view).showUserDetailsInViews(userDetailModel);
  }
}
