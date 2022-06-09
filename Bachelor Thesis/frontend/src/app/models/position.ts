export class Position {
  rank: number;
  file: number;

  constructor(rank: number, file: number) {
    this.rank = rank;
    this.file = file;
  }

  static equals(position1: Position, position2: Position): boolean {
    return position1.rank == position2.rank && position1.file == position2.file
  }
}
