package com.beinformed.research.labs.dialogprogress.testing;

import java.util.Random;
import java.util.UUID;


class Form {
	Random rnd = new Random();
	
	private abstract class FormQuestion {
		String question;
		String answer;
		private int std;
		private int avg;
		
		public FormQuestion(String question, int avg, int std) {
			this.question = question;
			this.avg = avg;
			this.std = std;
		}
		
		public String toString() {
			return "'[DataAnchor [" + question +"]]','DataAnchor [" + question + "=" + answer + "]'";
		}
		public int getDuration() {
			return (int) (avg + rnd.nextGaussian()*std);
		}
		
		public abstract FormQuestion getNext();
		public abstract void genAnswer();
	}

	FormQuestion age = new FormQuestion("person|age", 10, 5) {
		public FormQuestion getNext() {
			return Integer.parseInt(answer) > 65 ? job : null;
		}
		public void genAnswer() {
			answer = Integer.toString((int) (50 + rnd.nextGaussian()*30));
		}
	};
	FormQuestion job = new FormQuestion("job|worktime", 300, 30) {
		public FormQuestion getNext() {
			return Integer.parseInt(answer) > 35 ? null : ill;
		}
		public void genAnswer() {
			answer = Integer.toString((int) (30 + rnd.nextGaussian()*5));
		}
	};
	FormQuestion ill = new FormQuestion("person|ill", 30, 10) {
		public FormQuestion getNext() {
			return answer.equals("true") ? home : children;
		}
		public void genAnswer() {
			answer = Boolean.toString(rnd.nextBoolean());
		}
	};
	FormQuestion home = new FormQuestion("person|home", 10, 5) {
		public FormQuestion getNext() {
			return answer.equals("solo") ? help : null;
		}
		public void genAnswer() {
			answer = rnd.nextBoolean() ? "solo" : "group";
		}
	};
	FormQuestion children = new FormQuestion("person|children", 30, 10) {
		public FormQuestion getNext() {
			return null;
		}
		public void genAnswer() {
			answer = rnd.nextBoolean() ? "professional" : "none";
		}
	};
	FormQuestion help = new FormQuestion("person|help", 70, 5) {
		public FormQuestion getNext() {
			return answer.equals("professional") ? helpfreq : null;
		}
		public void genAnswer() {
			answer = rnd.nextBoolean() ? "professional" : "none";
		}
	};
	FormQuestion helpfreq = new FormQuestion("person|helpfreq", 120, 10) {
		public FormQuestion getNext() {
			return null;
		}
		public void genAnswer() {
			answer = Integer.toString((int) (3 + rnd.nextGaussian()));
		}
	};
	
	
	public String generateObservation() {
		FormQuestion next = age;
		long time = System.currentTimeMillis();
		UUID guid = UUID.randomUUID();
		String result = "";
		
		while(next != null) {
			next.genAnswer();
			result += "TESTFORM," + guid.toString() + "," + time + "," + next.toString() + ",OK\n";
			time += next.getDuration();
			next = next.getNext();
		}
		
		return result;
	}
	
	
}