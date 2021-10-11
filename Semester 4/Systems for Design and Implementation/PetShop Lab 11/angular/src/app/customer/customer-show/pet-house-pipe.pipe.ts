import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'petHousePipe'
})
export class PetHousePipePipe implements PipeTransform {

  transform(value: any, ...args: any[]): string {
    if (value == null)
      return "No pet house"
    return `Color: ${value.color}, Size: ${value.size}`
  }

}
