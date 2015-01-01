package beans.estrututra.him;

import java.io.Serializable;

public class TopHashtagHIM implements Serializable
{
	private static final long serialVersionUID = -961964456584206072L;

	private long ranking;
	private String hashtag;
	private long nrPost;

	public TopHashtagHIM() {}

	public long getRanking() {
		return ranking;
	}

	public void setRanking(long ranking) {
		this.ranking = ranking;
	}

	public String getHashtag() {
		return hashtag;
	}

	public void setHashtag(String hashtag) {
		this.hashtag = hashtag;
	}

	public long getNrPost() {
		return nrPost;
	}

	public void setNrPost(long nrPost) {
		this.nrPost = nrPost;
	}

	
}
