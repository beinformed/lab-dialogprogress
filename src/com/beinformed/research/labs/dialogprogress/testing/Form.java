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
			return "'[DataAnchor [" + question +"]]','DataAnchor [" + question + "]=" + answer + "'";
		}
		public int getDuration() {
			return (int) (avg + rnd.nextGaussian()*std) * 1000;
		}
		
		public abstract FormQuestion getNext();
		public abstract void genAnswer();
	}

	FormQuestion age = new FormQuestion("person|age", 10, 5) {
		public FormQuestion getNext() {
			int value = Integer.parseInt(answer);
			if(value > 65)
				return yworked;
			if(value > 30)
				return job_a;
			return study;
		}
		public void genAnswer() {
			answer = Integer.toString((int) (50 + rnd.nextGaussian()*30));
		}
	};
	FormQuestion study = new FormQuestion("person|study", 300, 20) {
		@Override
		public FormQuestion getNext() {
			if(Boolean.parseBoolean(answer))
				return study_time;
			else
				return job_b;
		}
		@Override
		public void genAnswer() {
			answer = Boolean.toString(rnd.nextBoolean());
		}		
	};
	FormQuestion job_a = new FormQuestion("person|job", 60, 5) {
		@Override
		public FormQuestion getNext() {
			if(Boolean.parseBoolean(answer))
				return salary_a;
			else
				return null;
		}
		@Override
		public void genAnswer() {
			answer = Boolean.toString(rnd.nextBoolean());
		}
	};
	FormQuestion job_b = new FormQuestion("person|job", 60, 5) {
		@Override
		public FormQuestion getNext() {
			return have_degree;
		}
		@Override
		public void genAnswer() {
			answer = Boolean.toString(rnd.nextBoolean());
		}
	};
	FormQuestion study_time = new FormQuestion("person|study_time", 10, 1) {

		@Override
		public FormQuestion getNext() {
			return have_degree;
		}

		@Override
		public void genAnswer() {
			answer = Boolean.toString(rnd.nextBoolean());
		}
		
	};
	FormQuestion have_degree = new FormQuestion("person|have_degree", 30, 2) {

		@Override
		public FormQuestion getNext() {
			return null;
		}

		@Override
		public void genAnswer() {
			answer = Boolean.toString(rnd.nextBoolean());
		}
		
	};
	FormQuestion yworked = new FormQuestion("person|years_worked", 60, 10) {

		@Override
		public FormQuestion getNext() {
			int value = Integer.parseInt(answer);
			if(value > 40)
				return salary_b;
			else
				return working;
		}

		@Override
		public void genAnswer() {
			answer = Integer.toString( (int) (30 + rnd.nextGaussian()*5));
		}
		
	};
	FormQuestion salary_a = new FormQuestion("person|salary", 120, 20) {

		@Override
		public FormQuestion getNext() {
			int value = Integer.parseInt(answer);
			if(value > 20000)
				return null;
			else
				return no_jobs;
		}

		@Override
		public void genAnswer() {
			answer = Integer.toString((int) (15000 + rnd.nextGaussian()*2000));
		}};
	FormQuestion salary_b = new FormQuestion("person|salary", 180, 20) {

		@Override
		public FormQuestion getNext() {
			return other_country;
		}

		@Override
		public void genAnswer() {
			answer = Integer.toString((int) (15000 + rnd.nextGaussian()*2000));			
		} };
	FormQuestion no_jobs = new FormQuestion("person|no_jobs", 60, 10) {

		@Override
		public FormQuestion getNext() {
			return null;
		}

		@Override
		public void genAnswer() {
			answer = Integer.toString(1 + rnd.nextInt(10));
		} };
	FormQuestion other_country = new FormQuestion("person|other_country", 10, 1) {

		@Override
		public FormQuestion getNext() {
			if(Boolean.parseBoolean(answer))
				return time_other_country;
			else
				return null;
		}

		@Override
		public void genAnswer() {
			answer = Boolean.toString(rnd.nextBoolean());
		} };
	FormQuestion time_other_country = new FormQuestion("person|time_other_country", 60, 2) {

		@Override
		public FormQuestion getNext() {
			return null;
		}

		@Override
		public void genAnswer() {
			answer = Integer.toString(rnd.nextInt(5));
		} };
	FormQuestion working = new FormQuestion("person|working", 10, 1) {

		@Override
		public FormQuestion getNext() {
			return have_children;
		}

		@Override
		public void genAnswer() {
			answer = Boolean.toString(rnd.nextBoolean());
		}
		
	};
	FormQuestion have_children = new FormQuestion("person|have_children", 5, 1) {

		@Override
		public FormQuestion getNext() {
			if(Boolean.parseBoolean(answer))
				return null;
			else
				return live_alone;
		}

		@Override
		public void genAnswer() {
			answer = Boolean.toString(rnd.nextBoolean());
		} };
	FormQuestion live_alone = new FormQuestion("person|live_alone", 5, 1) {

		@Override
		public FormQuestion getNext() {
			return null;
		}

		@Override
		public void genAnswer() {
			answer = Boolean.toString(rnd.nextBoolean());			
		} };	
		
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