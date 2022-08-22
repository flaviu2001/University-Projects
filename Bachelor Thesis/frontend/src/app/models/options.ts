export class Options {
  alphaBeta: boolean
  quiescence: boolean
  transposition: boolean
  parallelization: boolean
  lowCutoff: number
  highCutoff: number


  constructor(alphaBeta: boolean, quiescence: boolean, transposition: boolean, parallelization: boolean, lowCutoff: number, highCutoff: number) {
    this.alphaBeta = alphaBeta;
    this.quiescence = quiescence;
    this.transposition = transposition;
    this.parallelization = parallelization;
    this.lowCutoff = lowCutoff;
    this.highCutoff = highCutoff;
  }
}
